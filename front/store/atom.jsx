import { atom, selector } from "recoil";

export const loginState = atom({
  key: "loginState",
  default: selector({
    key: "loginState/Default",
    get: () => {
      if (typeof window !== "undefined") {
        const isToken = localStorage.getItem('access_token');
        return {
          isLogin: isToken ? true : false,
        };
      } else {
        return {
          isLogin: false,
        };
      }
    },
  }),
});

export const itemListState = atom({
  key: "itemListState",
  default: []
});


export const likedItemsState = atom({
  key: 'likedItemsState',
  default: {},
});
