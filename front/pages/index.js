import React, { useEffect } from 'react';
import { useRouter } from 'next/router';
import { useRecoilValue } from 'recoil';
import { loginState } from '@/store/atom';

function Home() {
    const router = useRouter();
    const isLogin = useRecoilValue(loginState);
    console.log('isLogin',isLogin)
    useEffect(() => {
        const token = localStorage.getItem('access_token');
        if (isLogin && token) {
            router.replace('/main');
        } else {
            router.replace('/login');
        }
    }, [isLogin, router]);

    return (
        <div>Redirecting...</div>
    );
}

export default Home;
