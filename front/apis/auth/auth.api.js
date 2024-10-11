import { api } from '../api';
import axios from 'axios';

export const test = async () => {
    try {
      const response = await axios.get(process.env.NEXT_PUBLIC_API_BASE_URL);
      return response.data;
    } catch (error) {
      console.error('테스트 실패:', error);
      throw error;
    }
  };
  

export const login = async (email, password) => {
    try {
      const response = await api.post({
        url: '/users/login',
        data: { email, password },
      });
      return response.data;
    } catch (error) {
      console.error('로그인 실패:', error);
      throw error;
    }
  };

export const register = async (username, email, password) => {
  try {
    const response = await api.post({
      url: '/users',
      data: { username, email, password },
    });
    return response.data;
  } catch (error) {
    console.error('회원가입 실패:', error);
    throw error;
  }
};

export const refreshToken = async (refreshToken) => {
  try {
    const response = await api.post({
      url: '/users/token/reissue',
      data: { refreshToken },
    });
    return response.data;
  } catch (error) {
    console.error('토큰 갱신 실패:', error);
    throw error;
  }
};

export const logout = async (refreshToken) => {
  try {
    const response = await api.del({
      url: '/users/logout',
      data: { refreshToken },
    });
    return response.data;
  } catch (error) {
    console.error('로그아웃 실패:', error);
    throw error;
  }
};

export const getUserInfo = async () => {
  try {
    const response = await api.get({
      url: '/users/profile',
    });
    return response.data;
  } catch (error) {
    console.error('유저정보 조회 실패:', error);
    throw error;
  }
};

export const userCheckName = async () => {
  try {
    const response = await api.get({
      url: '/users/check-name',
    });
    return response.data;
  } catch (error) {
    console.error('이름 중복 체크 실패:', error);
    throw error;
  }
};

export const changePassword = async (currentPassword,newPassword,confirmNewPassword) => {
  try {
    const response = await api.patch({
      url: '/users/password',
      data: { currentPassword,newPassword,confirmNewPassword },
    });
    return response.data;
  } catch (error) {
    console.error('비밀번호 변경 실패:', error);
    throw error;
  }
};


export const changeProfile = async (profileImage, username) => {
  try {
    const formData = new FormData();

    if (profileImage) {
      formData.append('profileImage', profileImage);
    }

    // 쿼리 파라미터로 이름 전달
    const response = await api.mpatch({
      url: `/users/profile?name=${username}`,
      data: formData,
    });

    console.log('프로필 수정 성공:', response);
    return response.data;
  } catch (error) {
    console.error('프로필 변경 실패:', error);
    throw error;
  }
};

