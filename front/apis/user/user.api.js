import { api } from '../api';

export const getUserinfo = async (userId) => {
    try {
      const response = await api.get({
        url: `/users/profile/${userId}`,
      });
      return response.data;
    } catch (error) {
      console.error('유저 프로필 조회 실패:', error);
      throw error;
    }
  };

  export const userFollow = async (targetUserId) => {
    try {
      const response = await api.post({
        url: `/users/${targetUserId}/follow`,
      });
      return response.data;
    } catch (error) {
      console.error('팔로워 실패:', error.response);
      throw error;
    }
  };


  export const userUnfollow = async (targetUserId) => {
    try {
      const response = await api.del({
        url: `/users/${targetUserId}/unfollow`,
      });
      return response.data;
    } catch (error) {
      console.error('언팔로워 실패:', error.response);
      throw error;
    }
  };