import { api } from '../api';

export const getItems = async () => {
    try {
      const response = await api.get({
        url: '/posts',
      });
      return response.data;
    } catch (error) {
      console.error('아이탬가져오기 실패:', error);
      throw error;
    }
  };


  export const postItems = async (title, content, images = []) => {
    try {
        const formData = new FormData();
        
        // post 객체를 만들어서 FormData에 추가
        const post = { title, content };
        formData.append('post', JSON.stringify(post)); // post 객체를 JSON 문자열로 변환하여 전송

        // 각 이미지를 FormData에 추가
        images.forEach((image) => {
            formData.append('images', image);  // 이미지 배열로 전송
        });

        const response = await api.mpost({
            url: '/posts',
            data: formData,
        });
        
        return response.data;
    } catch (error) {
        console.error('게시글 업로드 실패:', error.response);
        throw error;
    }
};


export const getSearchTitle = async (title) => {
  try {
    const response = await api.get({
      url:`/search/title?title=${title}`
    });
    console.log(response)
    return response.data || [];
  } catch (error) {
    console.error('title 검색 가져오기 실패:', error);
    throw error;
  }
};

export const getSearchAuthor = async (author) => {
  try {
    const response = await api.get({
      url:`/search/author?username=${author}`
    });
    console.log(response)
    return response.data || [];
  } catch (error) {
    console.error('author 검색 가져오기 실패:', error);
    throw error;
  }
};

  
  

  export const uploadImg = async (id) => {
    try {
      const response = await api.post({
        url: `/s3/upload`,
      });
      return response.data;
    } catch (error) {
      console.error('이미지 등록 실패:', error.response);
      throw error;
    }
  };


  export const deleteImg = async (id) => {
    try {
      const response = await api.del({
        url: `/s3/delte`,
      });
      return response.data;
    } catch (error) {
      console.error('이미지 삭제 실패:', error.response);
      throw error;
    }
  };


  export const patchItems = async (id,title, content) => {
    try {
      const response = await api.patch({
        url: `/posts/${id}`,
        data: { title, content },
      });
      return response.data;
    } catch (error) {
      console.error('아이탭 입력하기 실패:', error.response);
      throw error;
    }
  };


  export const deleteItems = async (id) => {
    try {
      const response = await api.del({
        url: `/posts/${id}`,
      });
      return response.data;
    } catch (error) {
      console.error('아이탭 삭제 실패:', error.response);
      throw error;
    }
  };


  export const detailItems = async (id) => {
    try {
      const response = await api.get({
        url: `/posts/${id}`,
      });
      return response.data;
    } catch (error) {
      console.error('아이탭 상세조회 실패:', error.response);
      throw error;
    }
  };

  export const itemLike = async (id) => {
    try {
      const response = await api.post({
        url: `/posts/${id}/like`,
      });
      return response.data;
    } catch (error) {
      console.error('아이탭 좋아 실패:', error.response);
      throw error;
    }
  };


  export const itemUnlike = async (id) => {
    try {
      const response = await api.post({
        url: `/posts/${id}/unlike`,
      });
      return response.data;
    } catch (error) {
      console.error('아이탭 싫어 실패:', error.response);
      throw error;
    }
  };
