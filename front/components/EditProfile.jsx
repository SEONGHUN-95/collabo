import React, { useState } from 'react';
import styled from 'styled-components';
import { changeProfile } from '@/apis/auth/auth.api';
import { useRouter } from 'next/router';

const EditProfile = ({ userInfo,setUserInfo }) => {
    const [username, setUsername] = useState(userInfo.username);
    const [profileImage, setProfileImage] = useState(null);
    const [message, setMessage] = useState('');
    const router = useRouter();

    const handleImageChange = (e) => {
        setProfileImage(e.target.files[0]); 
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        console.log('username, profileImage',username)
        try {
            const result = await changeProfile(profileImage,username);
            // 성공 메시지 표시 및 페이지 이동
            setMessage('프로필이 성공적으로 수정되었습니다.');
            if (result) {
                setUserInfo((prev) => ({
                    ...prev,
                    username: username,
                    profileImage: profileImage ? URL.createObjectURL(profileImage) : prev.profileImage, // 이미지 업데이트
                }));
                setTimeout(() => {
                    router.push('/'); // 1.5초 후 메인 페이지로 이동
                }, 1500);
            }
        } catch (error) {
            console.error('프로필 수정 실패:', error);
            setMessage('프로필 수정에 실패했습니다.');
        }
    };

    return (
        <Content>
            <ProfileCard>
                <form onSubmit={handleSubmit}>
                    <ProfileImage src={userInfo.profileImage || 'https://via.placeholder.com/150'} alt="Profile" />
                    <InputLabel>이름 변경:</InputLabel>
                    <StyledInput
                        type="text"
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                    />
                    <InputLabel>프로필 이미지 변경:</InputLabel>
                    <StyledInput type="file" onChange={handleImageChange} />
                    <SubmitButton type="submit">저장</SubmitButton>

                    {/* 메시지 표시 */}
                    {message && <Message>{message}</Message>}
                </form>
            </ProfileCard>
        </Content>
    );
};

export default EditProfile;

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

const InputLabel = styled.label`
    font-weight: bold;
    margin-top: 20px;
    display: block;
    color: #555;
`;

const StyledInput = styled.input`
    padding: 10px;
    margin-top: 10px;
    border-radius: 8px;
    width: 100%;
    border: 2px solid #ddd;
    transition: all 0.3s ease;

    &:focus {
        border-color: #007bff;
        outline: none;
        box-shadow: 0 0 5px rgba(0, 123, 255, 0.5);
    }
`;

const SubmitButton = styled.button`
    background-color: #007bff;
    color: white;
    padding: 12px 20px;
    margin-top: 30px;
    border: none;
    border-radius: 30px;
    cursor: pointer;
    transition: background-color 0.3s ease;

    &:hover {
        background-color: #0056b3;
    }
`;

const Message = styled.p`
    color: ${(props) => (props.success ? 'green' : 'red')};
    font-size: 14px;
    margin-top: 10px;
`;

const ProfileImage = styled.img`
    width: 150px;
    height: 150px;
    border-radius: 50%;
    margin-bottom: 20px;
    object-fit: cover;
`;
