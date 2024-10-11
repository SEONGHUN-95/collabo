import React from 'react';
import styled from 'styled-components';

const ProfileInfo = ({ userInfo }) => (
    <Content>
        <ProfileCard>
            <ProfileImage src={userInfo.profileImage || 'https://via.placeholder.com/150'} alt="Profile" />
            <InfoRow>
                <Label>이름:</Label>
                <Value>{userInfo.username}</Value>
            </InfoRow>
            <InfoRow>
                <Label>이메일:</Label>
                <Value>{userInfo.email}</Value>
            </InfoRow>
            <InfoRow>
                <Label>팔로워:</Label>
                <Value>{userInfo.followers?.length || 0}</Value> {/* 배열 길이로 팔로워 수 표시 */}
            </InfoRow>
            <InfoRow>
                <Label>팔로잉:</Label>
                <Value>{userInfo.following?.length || 0}</Value> {/* 배열 길이로 팔로잉 수 표시 */}
            </InfoRow>
        </ProfileCard>
    </Content>
);

export default ProfileInfo;

// 스타일 정의
const Content = styled.div`
    display: flex;
    flex-direction: column;
    align-items: center;
    width: 100%;
`;

const ProfileCard = styled.div`
    background: white;
    padding: 30px;
    border-radius: 15px;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
    width: 100%;
    max-width: 500px;
`;

const InfoRow = styled.div`
    display: flex;
    justify-content: space-between;
    width: 100%;
    margin-bottom: 15px;
`;

const Label = styled.span`
    font-weight: bold;
    color: #555;
    font-size: 16px;
`;

const Value = styled.span`
    color: #333;
    font-size: 16px;
`;

const ProfileImage = styled.img`
    width: 150px;
    height: 150px;
    border-radius: 50%;
    margin-bottom: 20px;
    object-fit: cover;
`;
