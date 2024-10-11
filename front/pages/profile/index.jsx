import React, { useState, useEffect } from 'react';
import styled from 'styled-components';
import { getUserInfo } from '@/apis/auth/auth.api';
import ChangePassword from '@/components/ChangePassword';
import ProfileInfo from '@/components/ProfileInfo';
import EditProfile from '@/components/EditProfile';
function Profile() {
    const [activeTab, setActiveTab] = useState('profile'); // 기본 탭은 프로필 보기
    const [userInfo, setUserInfo] = useState({}); // 사용자 정보 저장

    useEffect(() => {
        const fetchUserInfo = async () => {
            try {
                const data = await getUserInfo();
                console.log('datadbwj',data)
                setUserInfo(data);
            } catch (error) {
                console.error('유저 정보 가져오기 실패:', error);
            }
        };
        fetchUserInfo();
    }, []);

    const handleTabClick = (tab) => {
        setActiveTab(tab);
    };

    return (
        <Container>
            <TabContainer>
                <TabButton
                    isActive={activeTab === 'profile'}
                    onClick={() => handleTabClick('profile')}
                >
                    프로필 정보
                </TabButton>
                <TabButton
                    isActive={activeTab === 'edit'}
                    onClick={() => handleTabClick('edit')}
                >
                    프로필 수정
                </TabButton>
                <TabButton
                    isActive={activeTab === 'password'}
                    onClick={() => handleTabClick('password')}
                >
                    비밀번호 변경
                </TabButton>
            </TabContainer>
            {activeTab === 'profile' && <ProfileInfo userInfo={userInfo} />}
            {activeTab === 'edit' && <EditProfile userInfo={userInfo} setUserInfo={setUserInfo}/>}
            {activeTab === 'password' && <ChangePassword />}
        </Container>
    );
}



export default Profile;

const Container = styled.div`
    display: flex;
    flex-direction: column;
    align-items: center;
    padding: 40px;
    background-color: #f8f9fa;
    margin-top: 50px;
    width: 100%;
`;

const TabContainer = styled.div`
    display: flex;
    justify-content: center;
    margin-bottom: 30px;
`;

const TabButton = styled.button`
    background-color: ${(props) => (props.isActive ? '#007bff' : '#fff')};
    color: ${(props) => (props.isActive ? '#fff' : '#007bff')};
    border: 2px solid #007bff;
    padding: 12px 25px;
    margin: 0 10px;
    cursor: pointer;
    border-radius: 30px;
    font-size: 16px;
    font-weight: bold;
    transition: all 0.3s ease;

    &:hover {
        background-color: #0056b3;
        color: white;
    }
`;
