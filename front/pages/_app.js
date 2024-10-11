import { RecoilRoot, useRecoilValue } from 'recoil';
import { useRouter } from 'next/router';
import Header from '@/components/Header';
import '@/styles/global.css';
import { loginState } from '@/store/atom'; 
import { useEffect } from 'react';

function MyApp({ Component, pageProps }) {
  return (
    <RecoilRoot>
      <AppContent Component={Component} pageProps={pageProps} />
    </RecoilRoot>
  );
}

function AppContent({ Component, pageProps }) {
  const router = useRouter();
  const isLogin = useRecoilValue(loginState);

  useEffect(() => {
    if (!isLogin) {
      router.replace('/login');
    }
  }, [isLogin, router]);
  
  const isLoginPage = router.pathname === '/login';

  return (
    <>
      {!isLoginPage && isLogin && <Header />}
      <Component {...pageProps} />
    </>
  );
}

export default MyApp;