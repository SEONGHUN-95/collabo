import React, { useState } from 'react';
import styled from 'styled-components';
import withAuth from '@/hooks/withAuth';
import { useRecoilState } from "recoil";
import { postItems } from "@/apis/item/item.api";
import { itemListState } from "@/store/atom";
import { useRouter } from 'next/router';

function Posts() {
    const [title, setTitle] = useState('');
    const [content, setContent] = useState('');
    const [images, setImages] = useState([]); // 이미지 상태 추가
    const [items, setItems] = useRecoilState(itemListState);
    const router = useRouter();

    const handleImageChange = (e) => {
        const files = Array.from(e.target.files);
        setImages(files);
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            // postItems API 호출
            const result = await postItems(title, content, images);
            setItems([result, ...items]);
            setTitle('');
            setContent('');
            setImages([]);
            router.push('/main');
        } catch (error) {
            console.error('게시글 업로드 실패:', error);
        }
    };

    return (
        <Container>
            <Form onSubmit={handleSubmit}>
                <InputWrapper>
                    <Label>제목</Label>
                    <Input
                        type="text"
                        value={title}
                        onChange={(e) => setTitle(e.target.value)}
                        placeholder="제목을 입력해주세요"
                        required
                    />
                </InputWrapper>
                <InputWrapper>
                    <Label>내용</Label>
                    <TextArea
                        value={content}
                        onChange={(e) => setContent(e.target.value)}
                        placeholder="내용을 입력해주세요"
                        rows={10}
                        required
                    />
                </InputWrapper>
                <InputWrapper>
                    <Label>이미지 업로드</Label>
                    <Input
                        type="file"
                        onChange={handleImageChange}
                        multiple
                    />
                </InputWrapper>
                <Button type="submit">글 올리기</Button>
            </Form>
        </Container>
    );
}

export default withAuth(Posts);

// Styled-components를 사용한 스타일링

const Container = styled.div`
    display: flex;
    flex-direction: column;
    align-items: center;
    padding: 20px;
    background-color: #f0f2f5;
    margin-top: 100px;
`;

const Form = styled.form`
    display: flex;
    flex-direction: column;
    width: 100%;
    max-width: 600px;
    background-color: #fff;
    padding: 30px;
    border-radius: 10px;
    box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.1);
`;

const InputWrapper = styled.div`
    margin-bottom: 20px;
`;

const Label = styled.label`
    font-size: 16px;
    color: #333;
    margin-bottom: 8px;
    display: block;
`;

const Input = styled.input`
    width: 100%;
    padding: 10px;
    font-size: 16px;
    border-radius: 5px;
    border: 1px solid #ddd;
    box-sizing: border-box;

    &:focus {
        border-color: #888;
        outline: none;
    }
`;

const TextArea = styled.textarea`
    width: 100%;
    padding: 10px;
    font-size: 16px;
    border-radius: 5px;
    border: 1px solid #ddd;
    box-sizing: border-box;
    resize: vertical;

    &:focus {
        border-color: #888;
        outline: none;
    }
`;

const Button = styled.button`
    padding: 12px 20px;
    font-size: 16px;
    color: #fff;
    background-color: #007bff;
    border: none;
    border-radius: 5px;
    cursor: pointer;
    transition: background-color 0.3s ease;

    &:hover {
        background-color: #0056b3;
    }

    &:disabled {
        background-color: #ccc;
        cursor: not-allowed;
    }
`;
