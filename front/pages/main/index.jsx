import React, { useEffect, useState } from 'react';
import { getItems, deleteItems, patchItems, itemLike, itemUnlike } from "@/apis/item/item.api";
import { getUserInfo } from '@/apis/auth/auth.api';
import styled from 'styled-components';
import withAuth from '@/hooks/withAuth';
import { useRecoilState } from "recoil";
import { itemListState, likedItemsState } from "@/store/atom";
import { useRouter } from 'next/router';
import CommentBox from '@/components/CommentBox';
import Search from "@/components/Search"; // Search 컴포넌트 가져오기
import UserInfoModal from '@/components/UserInfoModal';

function MainPage() {
    const router = useRouter();
    const [items, setItems] = useRecoilState(itemListState);
    const [likedItems, setLikedItems] = useRecoilState(likedItemsState); 
    const [editingId, setEditingId] = useState(null);
    const [newTitle, setNewTitle] = useState('');
    const [newContent, setNewContent] = useState(''); 
    const [username, setUsername] = useState('');
    const [isImageModalOpen, setIsImageModalOpen] = useState(false); // 이미지 모달 상태
    const [isUserModalOpen, setIsUserModalOpen] = useState(false);  // 사용자 모달 상태
    const [selectedImage, setSelectedImage] = useState(null); // 선택된 이미지
    const [selectedUser, setSelectedUser] = useState(null);  // 선택된 사용자 정보
    const [selectedUserId, setSelectedUserId] = useState(null);
    const [noResultsMessage, setNoResultsMessage] = useState('');
    useEffect(() => {
        handleGetItems();
        fetchUserInfo();

        const savedLikes = localStorage.getItem('likedItems');
        if (savedLikes) {
            setLikedItems(JSON.parse(savedLikes));
        }
    }, []);

    const fetchUserInfo = async () => {
        try {
            const result = await getUserInfo();
            setUsername(result.username); 
        } catch (error) {
            console.error('유저 정보 조회 실패:', error);
        }
    };

    const handleGetItems = async () => {
        try {
            const result = await getItems();
            const sortedItems = result.sort((a, b) => b.id - a.id);
            console.log('sortedItems',sortedItems)
            setItems(sortedItems);
        } catch (error) {
            console.error('아이템 가져오기 오류:', error);
        }
    };

    const handleEdit = (e, id, title, content) => {
        e.stopPropagation();
        setEditingId(id);  // 수정 모드로 전환
        setNewTitle(title);
        setNewContent(content);
    };

    const handleSaveEdit = async (e, id) => {
        e.stopPropagation();
        try {
            await patchItems(id, newTitle, newContent);  // 수정할 아이템의 ID와 내용을 함께 전달
            setEditingId(null);  // 수정 모드 해제
            handleGetItems();  // 수정 후 아이템 목록 갱신
        } catch (error) {
            console.error('아이템 수정 실패:', error);
        }
    };

    const handleDelete = async (e, id) => {
        e.stopPropagation();
        try {
            await deleteItems(id);  // 서버에서 아이템 삭제
            handleGetItems();  // 삭제 후 아이템 목록 갱신
        } catch (error) {
            console.error('아이템 삭제 실패:', error);
        }
    };

    const handleLikeToggle = async (e, id, likeCount) => {
        e.stopPropagation();
        try {
            let newLikeCount = likeCount;
            let isLiked = likedItems[id] || false;

            if (isLiked) {
                await itemUnlike(id);
                newLikeCount -= 1;
            } else {
                await itemLike(id);
                newLikeCount += 1;
            }

            setItems((prevItems) =>
                prevItems.map((item) =>
                    item.id === id ? { ...item, likeCount: newLikeCount } : item
                )
            );

            const updatedLikedItems = {
                ...likedItems,
                [id]: !isLiked
            };
            setLikedItems(updatedLikedItems);

            localStorage.setItem('likedItems', JSON.stringify(updatedLikedItems));
        } catch (error) {
            console.error('좋아요 토글 오류:', error);
        }
    };

    const handleSearchResults = (results, errorMessage) => {
        if (errorMessage) {
          setNoResultsMessage(errorMessage);  // 에러 메시지가 있을 경우 설정
        } else if (results.length === 0) {
          setNoResultsMessage('검색 결과가 없습니다.');
        } else {
          setNoResultsMessage(''); // 결과가 있을 경우 메시지 초기화
        }
        setItems(results); // 결과 업데이트
      };
      

    // 이미지 클릭 시 모달 열기
    const openImageModal = (imageUrl) => {
        setSelectedImage(imageUrl);
        setIsImageModalOpen(true);
    };

    // 이미지 모달 닫기
    const closeImageModal = () => {
        setIsImageModalOpen(false);
        setSelectedImage(null);
    };

    const handleOpenUserModal = (userId) => {
        setSelectedUserId(userId);
        setIsUserModalOpen(true);
    };

    const handleCloseUserModal = () => {
        setIsUserModalOpen(false);
        setSelectedUserId(null);
    };

    return (
        <Container>
            <Search onSearchResults={handleSearchResults} /> 
            {noResultsMessage && <NoResultsMessage>{noResultsMessage}</NoResultsMessage>}
            {items?.map((item) => (
                <ItemCard key={item.id}>
                    <ContentContainer>
                        {editingId === item.id ? (
                            <div>
                                <Input
                                    value={newTitle}
                                    onClick={(e) => e.stopPropagation()} 
                                    onChange={(e) => setNewTitle(e.target.value)}
                                />
                                <TextArea
                                    value={newContent}
                                    onClick={(e) => e.stopPropagation()} 
                                    onChange={(e) => setNewContent(e.target.value)}
                                />
                                <ButtonContainer>
                                    <Button onClick={(e) => setEditingId(null)}>취소</Button>
                                    <Button onClick={(e) => handleSaveEdit(e, item.id)}>저장</Button>
                                </ButtonContainer>
                            </div>
                        ) : (
                            <>
                                <Header>
                                    <HeaderInner>
                                    <ProfilePicture onClick={() => handleOpenUserModal(item.userId)} src={item.profileImageUrl? item.profileImageUr :'https://i.pravatar.cc/50?img'} alt={`${item.username}'s profile`} />
                                    <Username>{item.username}</Username>
                                    </HeaderInner>
                                    <Email>{item.createdAt}</Email>
                                </Header>
                                <Title>{item.title}</Title>
                                <Content>{item.content}</Content>
                                {item.imageUrls && item.imageUrls.length > 0 && (
                                    <ImageContainer>
                                        {item.imageUrls.map((url, index) => (
                                            <Image 
                                            onClick={() => openImageModal(url)}  // 이미지 모달 열기
                                            key={index} src={url} alt={`image-${index}`} />
                                        ))}
                                    </ImageContainer>
                                )}
                                <Footer>
                                    <LikeCount>좋아요: {item.likeCount}</LikeCount>
                                    <ButtonContainer>
                                        <LikeButton
                                            onClick={(e) => handleLikeToggle(e, item.id, item.likeCount)}
                                        >
                                            {likedItems[item.id] ? '좋아요 취소' : '좋아요'}
                                        </LikeButton>
                                        {item.username === username && (
                                            <>
                                                <Button onClick={(e) => handleEdit(e, item.id, item.title, item.content)}>수정</Button>
                                                <Button onClick={(e) => handleDelete(e, item.id)}>삭제</Button>
                                            </>
                                        )}
                                    </ButtonContainer>
                                </Footer>
                            </>
                        )}
                        <CommentBox postId={item.id} />
                    </ContentContainer>
                </ItemCard>
            ))}

            {/* 이미지 모달 */}
            {isImageModalOpen && (
                <ModalOverlay onClick={closeImageModal}>
                    <ModalContent>
                        <FullSizeImage src={selectedImage} alt="Full Size" />
                    </ModalContent>
                </ModalOverlay>
            )}

            {/* 사용자 정보 모달 */}
            <UserInfoModal 
                isOpen={isUserModalOpen}
                onClose={handleCloseUserModal}
                userId={selectedUserId}
                username={username}
            />
        </Container>
    );
}

export default withAuth(MainPage);





const NoResultsMessage = styled.p`
  color: red;
  font-size: 16px;
  margin-top: 120px;
`;

const Container = styled.div`
    display: flex;
    flex-direction: column;
    align-items: center;
    padding: 20px;
    margin-top: 50px;
`;

const ItemCard = styled.div`
    display: flex;
    width: 600px;
    /* max-width: 600px; */
    background-color: #fff;
    border: 1px solid #ddd;
    border-radius: 10px;
    box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.1);
    padding: 20px;
    margin-bottom: 20px;
    transition: box-shadow 0.3s ease;
    cursor: pointer;
    &:hover {
        box-shadow: 0px 6px 15px rgba(0, 0, 0, 0.2);
    }
`;

const ProfilePicture = styled.img`
    width: 50px;
    height: 50px;
    border-radius: 50%;
    margin-right: 20px;
`;

const ContentContainer = styled.div`
    flex: 1;
`;

const Header = styled.div`
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;
    flex-wrap: wrap;
`;

const HeaderInner = styled.div`
    /* background-color: red; */
    display: flex;
    align-items: center; 
`;

const Username = styled.span`
    font-size: 16px;
    font-weight: bold;
    color: #333;
`;

const Email = styled.span`
    font-size: 14px;
    color: #999;
`;

const Title = styled.h2`
    font-size: 18px;
    color: #333;
    margin-bottom: 5px;
`;

const Content = styled.p`
    word-break: break-word; /* 긴 단어나 문장이 줄바꿈 되도록 */
    white-space: normal; /* 텍스트가 한 줄에 쭉 이어지지 않도록 */
`;


const ImageContainer = styled.div`
    display: flex;
    flex-wrap: wrap;
    gap: 10px;
    margin-bottom: 15px;
`;

const Image = styled.img`
    max-width: 100%;
    height: auto;
    display: block;
    margin: 0 auto;
`;


const Footer = styled.div`
    display: flex;
    justify-content: space-between;
    align-items: center;
    flex-wrap: wrap;
`;

const LikeCount = styled.span`
    font-size: 14px;
    color: #777;
`;

const ButtonContainer = styled.div`
    display: flex;
    justify-content: flex-end;
    margin-top: 15px;
    flex-wrap: wrap;
`;

const Button = styled.button`
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

    &:disabled {
        background-color: #ccc;
        cursor: not-allowed;
    }

    & + & {
        margin-left: 10px;
    }
`;

const LikeButton = styled(Button)`
    background-color: #28a745;

    &:hover {
        background-color: #218838;
    }
`;

const Input = styled.input`
    width: 100%;
    padding: 10px;
    margin-bottom: 10px;
    border: 1px solid #ddd;
    border-radius: 5px;
    font-size: 16px;
`;

const TextArea = styled.textarea`
    width: 100%;
    padding: 10px;
    margin-bottom: 10px;
    border: 1px solid #ddd;
    border-radius: 5px;
    font-size: 16px;
    resize: vertical;
`;

const ModalOverlay = styled.div`
    position: fixed;
    top: 0;
    left: 0;
    width: 100vw;
    height: 100vh;
    background-color: rgba(0, 0, 0, 0.7);
    display: flex;
    justify-content: center;
    align-items: center;
    z-index: 1000;
    cursor: pointer;
`;

const ModalContent = styled.div`
    max-width: 80vw;
    max-height: 80vh;
    cursor: auto;
`;

const FullSizeImage = styled.img`
    max-width: 100%;
    max-height: 100%;
    object-fit: contain;
`;