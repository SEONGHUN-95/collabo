import axios from "axios";

// basic axios instance
export const axiosInstance = axios.create({
  baseURL: process.env.NEXT_PUBLIC_API_BASE_URL,
  headers: {
    "Access-Control-Allow-Origin": "*",
    "Content-Type": "application/json",
  },
});

axiosInstance.interceptors.request.use((config) => {
  const accessToken = localStorage.getItem("access_token");
  const refreshToken = localStorage.getItem("refresh_token");
  if (accessToken && config.headers) {
    config.headers["Authorization"] = `Bearer ${accessToken}`;
    config.headers["refreshtoken"] = refreshToken;
  }

  return config;
});

axiosInstance.interceptors.response.use(
  (response) => {
    // console.log(response, "@@@response@@@");
    return response;
  },
  async (error) => {
    console.log("error", error);
    const originalRequest = error.config;
    if (error.response.status === 401 && !originalRequest._retry) {
      originalRequest._retry = true;
      const refreshToken = localStorage.getItem("refresh_token");
      console.log(refreshToken, "refreshToken");
      const accessToken = localStorage.getItem("access_token");
      const response = await axios.post(
        `${process.env.NEXT_PUBLIC_API_BASE_URL}/users/token/reissue`,
        {},
        {
          headers: {
            Authorization: `Bearer ${accessToken}`,
            MemberId: memberId,
            RefreshToken: refreshToken,
          },
        }
      );
      console.log(response, "response");
      const newAccessToken = response.data.body.accessToken;
      localStorage.setItem("access_token", newAccessToken);

      originalRequest.headers["Authorization"] = `Bearer ${newAccessToken}`;
      originalRequest.headers["refreshtoken"] = refreshToken;
      return instance(originalRequest);
    }
    return Promise.reject(error);
  }
);


// multipart/form-data 용 axios 인스턴스
export const axiosMultipartInstance = axios.create({
  baseURL: process.env.NEXT_PUBLIC_API_BASE_URL,
  headers: {
    "Access-Control-Allow-Origin": "*",
    // Content-Type을 설정하지 않습니다. axios가 자동으로 처리하게 함
  },
});

axiosMultipartInstance.interceptors.request.use((config) => {
  const accessToken = localStorage.getItem("access_token");
  const refreshToken = localStorage.getItem("refresh_token");

  if (accessToken && config.headers) {
    config.headers["Authorization"] = `Bearer ${accessToken}`;
    config.headers["refreshtoken"] = refreshToken;
  }

  return config;
});

axiosMultipartInstance.interceptors.response.use(
  (response) => response,
  async (error) => {
    // 기존 axios 인스턴스와 동일한 로직 사용
    const originalRequest = error.config;
    if (error.response.status === 401 && !originalRequest._retry) {
      originalRequest._retry = true;
      const refreshToken = localStorage.getItem("refresh_token");
      const response = await axios.post(
        `${process.env.NEXT_PUBLIC_API_BASE_URL}/users/token/reissue`,
        {},
        {
          headers: {
            Authorization: `Bearer ${localStorage.getItem("access_token")}`,
            RefreshToken: refreshToken,
          },
        }
      );

      const newAccessToken = response.data.body.accessToken;
      localStorage.setItem("access_token", newAccessToken);

      originalRequest.headers["Authorization"] = `Bearer ${newAccessToken}`;
      originalRequest.headers["refreshtoken"] = refreshToken;
      return axiosMultipartInstance(originalRequest);
    }
    return Promise.reject(error);
  }
);
