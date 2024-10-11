import React, { useState } from 'react';
import styled from 'styled-components';
import { changePassword } from '@/apis/auth/auth.api';
import { useRouter } from 'next/router';
const ChangePassword = () => {
    const [currentPassword, setCurrentPassword] = useState('');
    const [newPassword, setNewPassword] = useState('');
    const [confirmNewPassword, setConfirmNewPassword] = useState('');
    const [errorMessage, setErrorMessage] = useState('');
    const [successMessage, setSuccessMessage] = useState('');
    const router = useRouter(); 

    const handleSubmit = async (e) => {
        e.preventDefault();

        // 비밀번호 일치 확인
        if (newPassword !== confirmNewPassword) {
            setErrorMessage('새 비밀번호와 비밀번호 확인이 일치하지 않습니다.');
            return;
        }

        try {
            const result = await changePassword(currentPassword, newPassword, confirmNewPassword);
            setSuccessMessage('비밀번호가 성공적으로 변경되었습니다.');
            setErrorMessage('');
            if(result){
                setTimeout(() => {
                    router.push('/'); 
                }, 1500);
            }
        } catch (error) {
            setErrorMessage('비밀번호 변경에 실패했습니다. 다시 시도해 주세요.');
            setSuccessMessage(''); 
        }
    };

    return (
        <Content>
            <ProfileCard>
                <form onSubmit={handleSubmit}>
                    <InputLabel>기존 비밀번호:</InputLabel>
                    <StyledInput
                        type="password"
                        value={currentPassword}
                        onChange={(e) => setCurrentPassword(e.target.value)}
                        required
                    />
                    <InputLabel>새 비밀번호:</InputLabel>
                    <StyledInput
                        type="password"
                        value={newPassword}
                        onChange={(e) => setNewPassword(e.target.value)}
                        required
                    />
                    <InputLabel>새 비밀번호 확인:</InputLabel>
                    <StyledInput
                        type="password"
                        value={confirmNewPassword}
                        onChange={(e) => setConfirmNewPassword(e.target.value)}
                        required
                    />
                    
                    {/* 에러 메시지 표시 */}
                    {errorMessage && <ErrorMessage>{errorMessage}</ErrorMessage>}
                    
                    {/* 성공 메시지 표시 */}
                    {successMessage && <SuccessMessage>{successMessage}</SuccessMessage>}
                    
                    <SubmitButton type="submit">변경</SubmitButton>
                </form>
            </ProfileCard>
        </Content>
    );
};

export default ChangePassword;

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
    width: 90%;
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

const ErrorMessage = styled.p`
    color: red;
    font-size: 14px;
    margin-top: 10px;
`;

const SuccessMessage = styled.p`
    color: green;
    font-size: 14px;
    margin-top: 10px;
`;
