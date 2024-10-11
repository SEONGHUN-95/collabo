import { useRouter } from 'next/router';
import { useEffect } from 'react';
import { useRecoilValue } from 'recoil';
import { loginState } from '@/store/atom';

const withAuth = (WrappedComponent) => {
  return (props) => {
    const router = useRouter();
    const isLogin = useRecoilValue(loginState);

    useEffect(() => {
      if (!isLogin) {
        // 로그인 상태가 아닐 경우, 로그인 페이지로 리다이렉트
        router.replace('/login');
      }
    }, [isLogin, router]);

    // 로그인 상태일 때만 컴포넌트를 렌더링
    return isLogin ? <WrappedComponent {...props} /> : null;
  };
};

export default withAuth;
