import React, { useEffect, useState } from 'react';
import styled from 'styled-components';
import { useRouter } from 'next/router';
import {test, login} from "@/apis/auth/auth.api";
import { useRecoilState } from "recoil";
import { loginState } from "@/store/atom";

const Login = () => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const router = useRouter();
  const [_, setIsLogin] = useRecoilState(loginState);

  useEffect(()=>{
    handleTest()
  },[])
  
  const handleTest = async () => {
    try {
      const userData = await test();
      console.log('api test21',userData)
    } catch (error) {
      console.error('api 오류:', error);
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    console.log('email, password',email, password)
    try {
      const result = await login(email, password);
      if(result){
        localStorage.setItem("access_token", result.accessToken);
        localStorage.setItem("refresh_token", result.refreshToken);
        setIsLogin({ isLogin: true });
        router.push('/main');
      }
    } catch (error) {
      console.error('로그인 오류:', error);
      alert('로그인에 실패했습니다. 이메일과 비밀번호를 확인하세요.');
    }
  };

  const handleSignUpClick = () => {
    router.push('/register');
  };


  return (
    <Container>
      <Form onSubmit={handleSubmit}>
        <Title>로그인</Title>
        <Input 
          type="email" 
          placeholder="이메일" 
          value={email} 
          onChange={(e) => setEmail(e.target.value)} 
        />
        <Input 
          type="password" 
          placeholder="비밀번호" 
          value={password} 
          onChange={(e) => setPassword(e.target.value)} 
        />
        <Button type="submit">로그인</Button>
        <SignUp onClick={handleSignUpClick}>회원가입</SignUp>
      </Form>
    </Container>
  );
};

export default Login;

const Container = styled.div`
  display: flex;
  height: 100vh;
  justify-content: center;
  align-items: center;
  background-color: #f0f2f5;
`;

const Form = styled.form`
  display: flex;
  flex-direction: column;
  padding: 30px;
  border-radius: 10px;
  background-color: #fff;
  box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.1);
  width: 300px;
`;

const Title = styled.h2`
  margin-bottom: 20px;
  text-align: center;
  color: #000;
`;

const Input = styled.input`
  padding: 10px;
  margin-bottom: 10px;
  border: 1px solid #ddd;
  border-radius: 5px;
  font-size: 14px;
`;

const Button = styled.button`
  padding: 10px;
  background-color: #0070f3;
  color: #fff;
  border: none;
  margin-bottom: 10px;
  border-radius: 5px;
  cursor: pointer;
  font-size: 16px;
  &:hover {
    background-color: #005bb5;
  }
`;

const SignUp = styled.p`
  text-align: center;
  margin-top: 10px;
  font-size: 14px;
  cursor: pointer;
  text-decoration: underline;
`