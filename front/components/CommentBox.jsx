import React, { useEffect, useState } from 'react';
import styled from 'styled-components';
import { getComment, postComment, deleteCommentLike, patchCommentLike, postCommentLike, postCommentUnlike } from '@/apis/comment/comment.api';
import { useRecoilState } from 'recoil';
import { likedItemsState } from '@/store/atom'; // Recoil 상태 가져오기

function CommentBox({ postId }) {
    const [comments, setComments] = useState([]);
    const [newComment, setNewComment] = useState('');
    const [editingCommentId, setEditingCommentId] = useState(null);
    const [editCommentContent, setEditCommentContent] = useState('');
    const [likedItems, setLikedItems] = useRecoilState(likedItemsState); // 좋아요 상태 관리

    useEffect(() => {
        fetchComments();
        loadLikedItemsFromLocalStorage(); // 페이지 로드 시 로컬 스토리지에서 좋아요 상태 복원
    }, []);

    const fetchComments = async () => {
        try {
            const result = await getComment(postId);
            setComments(result);
        } catch (error) {
            console.error('댓글 가져오기 실패:', error);
        }
    };

    // 로컬 스토리지에서 좋아요 상태를 로드
    const loadLikedItemsFromLocalStorage = () => {
        const savedLikedItems = localStorage.getItem('likedItems');
        if (savedLikedItems) {
            setLikedItems(JSON.parse(savedLikedItems));
        }
    };

    const handleAddComment = async () => {
        try {
            if (newComment.trim()) {
                await postComment(postId, { content: newComment });
                setNewComment('');
                fetchComments(); // 댓글 작성 후 갱신
            }
        } catch (error) {
            console.error('댓글 작성 실패:', error);
        }
    };

    const handleEditComment = async (commentId) => {
        try {
            await patchCommentLike(postId, commentId, { content: editCommentContent });
            setEditingCommentId(null);
            setEditCommentContent('');
            fetchComments(); // 댓글 수정 후 갱신
        } catch (error) {
            console.error('댓글 수정 실패:', error);
        }
    };

    const handleDeleteComment = async (commentId) => {
        try {
            await deleteCommentLike(postId, commentId);
            fetchComments(); // 댓글 삭제 후 갱신
        } catch (error) {
            console.error('댓글 삭제 실패:', error);
        }
    };

    const handleLikeToggle = async (commentId, likeCount) => {
        const isLiked = likedItems[commentId] || false; // Recoil 상태에서 좋아요 여부 확인
        let newLikeCount = likeCount;

        try {
            if (isLiked) {
                await postCommentUnlike(postId, commentId); // 좋아요 취소
                newLikeCount -= 1;
            } else {
                await postCommentLike(postId, commentId); // 좋아요 추가
                newLikeCount += 1;
            }

            // 댓글의 좋아요 상태 업데이트
            setComments((prevComments) =>
                prevComments.map((comment) =>
                    comment.id === commentId
                        ? { ...comment, likeCount: newLikeCount }
                        : comment
                )
            );

            // Recoil 상태 업데이트 및 로컬 스토리지에 저장
            const updatedLikedItems = {
                ...likedItems,
                [commentId]: !isLiked,
            };
            setLikedItems(updatedLikedItems);
            localStorage.setItem('likedItems', JSON.stringify(updatedLikedItems)); // 로컬 스토리지에 저장
        } catch (error) {
            console.error('댓글 좋아요 토글 실패:', error);
        }
    };

    return (
        <CommentContainer>
            <CommentInput
                value={newComment}
                onChange={(e) => setNewComment(e.target.value)}
                placeholder="댓글을 입력하세요..."
            />
            <AddButton onClick={handleAddComment}>댓글 추가</AddButton>
            <CommentList>
                {comments.map((comment) => (
                    <CommentItem key={comment.id}>
                        <CommentHeader>
                            <CommentUser>{comment.username}</CommentUser>
                        </CommentHeader>
                        {editingCommentId === comment.id ? (
                            <div>
                                <CommentInput
                                    value={editCommentContent}
                                    onChange={(e) => setEditCommentContent(e.target.value)}
                                />
                                <Button onClick={() => handleEditComment(comment.id)}>저장</Button>
                                <Button onClick={() => setEditingCommentId(null)}>취소</Button>
                            </div>
                        ) : (
                            <>
                                <CommentText>{comment.content}</CommentText>
                                <LikeCount>좋아요: {comment.likeCount}</LikeCount>
                                <ButtonContainer>
                                    <Button onClick={() => setEditingCommentId(comment.id)}>수정</Button>
                                    <Button onClick={() => handleDeleteComment(comment.id)}>삭제</Button>
                                    <LikeButton
                                        onClick={() => handleLikeToggle(comment.id, comment.likeCount)}
                                    >
                                        {likedItems[comment.id] ? '좋아요 취소' : '좋아요'}
                                    </LikeButton>
                                </ButtonContainer>
                            </>
                        )}
                    </CommentItem>
                ))}
            </CommentList>
        </CommentContainer>
    );
}

export default CommentBox;

const CommentContainer = styled.div`
    margin-top: 20px;
`;

const CommentHeader = styled.div`
    display: flex;
    justify-content: space-between;
    margin-bottom: 5px;
`;

const CommentUser = styled.span`
    font-weight: bold;
    color: #333;
`;

const CommentInput = styled.textarea`
    width: 100%;
    padding: 10px;
    font-size: 14px;
    border-radius: 5px;
    border: 1px solid #ddd;
    resize: none;
    box-sizing: border-box;
    margin-bottom: 10px;

    &:focus {
        border-color: #888;
        outline: none;
    }
`;

const AddButton = styled.button`
    padding: 8px 12px;
    font-size: 14px;
    color: #fff;
    background-color: #007bff;
    border: none;
    border-radius: 5px;
    cursor: pointer;
    transition: background-color 0.3s ease;

    &:hover {
        background-color: #0056b3;
    }
`;

const CommentList = styled.ul`
    list-style: none;
    padding: 0;
    margin-top: 15px;
`;

const CommentItem = styled.li`
    padding: 8px;
    border-bottom: 1px solid #ddd;
`;

const CommentText = styled.p`
    margin: 0;
    font-size: 14px;
    color: #555;
`;

const LikeCount = styled.span`
    font-size: 14px;
    color: #777;
`;

const ButtonContainer = styled.div`
    display: flex;
    justify-content: flex-start;
    margin-top: 10px;
`;

const Button = styled.button`
    padding: 5px 10px;
    font-size: 12px;
    color: #fff;
    background-color: #007bff;
    border: none;
    border-radius: 5px;
    cursor: pointer;
    transition: background-color 0.3s ease;
    margin-right: 5px;

    &:hover {
        background-color: #0056b3;
    }
`;

const LikeButton = styled(Button)`
    background-color: #28a745;

    &:hover {
        background-color: #218838;
    }
`;
