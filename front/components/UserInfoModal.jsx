import React, { useEffect, useState } from 'react';
import styled from 'styled-components';
import { getUserinfo, userFollow, userUnfollow } from '@/apis/user/user.api';

const UserInfoModal = ({ isOpen, onClose, userId, username }) => {
    const [userData, setUserData] = useState(null);
    const [isFollowing, setIsFollowing] = useState(false); // 팔로우 상태

    // 유저 정보를 불러오는 함수
    const fetchUserInfo = async () => {
        try {
            const data = await getUserinfo(userId);
            setUserData(data);

            // 팔로우 상태 확인
            if (data.followers.includes(username)) {
                setIsFollowing(true); // 이미 팔로우한 상태
            } else {
                setIsFollowing(false); // 팔로우하지 않은 상태
            }
        } catch (error) {
            console.error('유저 정보 가져오기 실패:', error);
        }
    };

    useEffect(() => {
        if (isOpen && userId) {
            fetchUserInfo(); // 모달이 열리면 유저 정보 호출
        }
    }, [isOpen, userId, username]);

    // 팔로우/언팔로우 버튼 클릭 시 실행될 함수
    const handleFollowToggle = async () => {
        try {
            if (isFollowing) {
                // 언팔로우 API 호출
                await userUnfollow(userId);
            } else {
                // 팔로우 API 호출
                await userFollow(userId);
            }
            // 팔로우/언팔로우 후 유저 정보 다시 불러오기
            fetchUserInfo(); // API 재호출로 최신 상태 반영
        } catch (error) {
            console.error('팔로우/언팔로우 실패:', error);
        }
    };

    if (!isOpen || !userData) return null;

    return (
        <ModalOverlay onClick={onClose}>
            <ModalContent onClick={(e) => e.stopPropagation()}>
                <CloseButton onClick={onClose}>×</CloseButton>
                <ProfileSection>
                    <ProfileImage src={userData.profileImage} alt="img" />
                    <ProfileInfo>
                        <h2>{userData.username}의 프로필</h2>
                        <p>이메일: {userData.email}</p>
                        <p>팔로워: {userData.followers.length}</p> {/* 최신 팔로워 수 */}
                        <p>팔로잉: {userData.following.length}</p> {/* 최신 팔로잉 수 */}
                        
                        {userData.username === username ? (
                            <p></p> 
                        ) : (
<FollowButton onClick={handleFollowToggle} $isFollowing={isFollowing}>
    {isFollowing ? '언팔로우' : '팔로우'}
</FollowButton>
                        )}
                    </ProfileInfo>
                </ProfileSection>
            </ModalContent>
        </ModalOverlay>
    );
};

export default UserInfoModal;

const ModalOverlay = styled.div`
    position: fixed;
    top: 0;
    left: 0;
    width: 100vw;
    height: 100vh;
    background-color: rgba(0, 0, 0, 0.6);
    display: flex;
    justify-content: center;
    align-items: center;
    z-index: 1000;
    cursor: pointer;
`;

const ModalContent = styled.div`
    background: white;
    padding: 30px;
    border-radius: 10px;
    box-shadow: 0 8px 16px rgba(0, 0, 0, 0.3);
    max-width: 500px;
    width: 100%;
    cursor: auto;
    position: relative;
    animation: fadeIn 0.3s ease-in-out;
    font-family: 'Arial', sans-serif;
`;

const CloseButton = styled.button`
    background: none;
    border: none;
    font-size: 24px;
    font-weight: bold;
    color: #555;
    position: absolute;
    top: 10px;
    right: 15px;
    cursor: pointer;
    &:hover {
        color: #333;
    }
`;

const ProfileSection = styled.div`
    display: flex;
    align-items: center;
    margin-bottom: 20px;
`;

const ProfileImage = styled.img`
    width: 100px;
    height: 100px;
    border-radius: 50%;
    margin-right: 20px;
    object-fit: cover;
    border: 2px solid #ddd;
`;

const ProfileInfo = styled.div`
    h2 {
        font-size: 24px;
        margin-bottom: 10px;
        color: #333;
    }

    p {
        margin: 4px 0;
        color: #666;
    }
`;

const FollowButton = styled.button`
    padding: 10px 20px;
    background-color: ${({ $isFollowing }) => ($isFollowing ? '#f44336' : '#4CAF50')};
    color: white;
    border: none;
    border-radius: 5px;
    cursor: pointer;
    &:hover {
        opacity: 0.9;
    }
`;
