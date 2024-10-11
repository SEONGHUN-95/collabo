import { api } from '../api';

export const getComment = async (postId) => {
    try {
      const response = await api.get({
        url: `/posts/${postId}/comments`,
      });
      return response.data;
    } catch (error) {
      console.error('댓글가져오기 실패:', error);
      throw error;
    }
  };


  export const postComment = async (postId, content) => {
    try {
      const response = await api.post({
        url: `/posts/${postId}/comments`,
        data: content,
      });
      return response.data;
    } catch (error) {
      console.error('대글작성 실패:', error);
      throw error;
    }
  };

  export const postCommentUnlike = async (postId,commentId) => {
    try {
      const response = await api.post({
        url: `/posts/${postId}/comments/${commentId}/unlike`,
      });
      return response.data;
    } catch (error) {
      console.error('대글작성 실패:', error);
      throw error;
    }
  };

  export const postCommentLike = async (postId,commentId) => {
    try {
      const response = await api.post({
        url: `/posts/${postId}/comments/${commentId}/like`,
      });
      return response.data;
    } catch (error) {
      console.error('대글작성 실패:', error);
      throw error;
    }
  };

  export const deleteCommentLike = async (postId,commentId) => {
    try {
      const response = await api.del({
        url: `/posts/${postId}/comments/${commentId}`,
      });
      return response.data;
    } catch (error) {
      console.error('대글작성 실패:', error);
      throw error;
    }
  };

  export const patchCommentLike = async (postId,commentId, content) => {
    try {
      const response = await api.patch({
        url: `/posts/${postId}/comments/${commentId}`,
        data: content,
      });
      return response.data;
    } catch (error) {
      console.error('대글작성 실패:', error);
      throw error;
    }
  };