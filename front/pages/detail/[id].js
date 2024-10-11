import { useRouter } from "next/router";
import { useEffect, useState } from "react";
import { detailItems } from "@/apis/item/item.api";
import styled from 'styled-components';

export default function Detail() {
    const router = useRouter();
    const { id } = router.query;
    const [item, setItem] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        if (id) {
            fetchItemDetail(id);
        }
    }, [id]);

    const fetchItemDetail = async (id) => {
        try {
            const result = await detailItems(id);
            setItem(result);
        } catch (error) {
            setError('Failed to load item details');
            console.error('Error fetching item details:', error);
        } finally {
            setLoading(false);
        }
    };

    if (loading) return <LoadingMessage>Loading...</LoadingMessage>;
    if (error) return <ErrorMessage>{error}</ErrorMessage>;

    return (
        <DetailContainer>
            {item ? (
                <>
                    <Title>{item.title}</Title>
                    <Content>{item.content}</Content>
                    <LikeCount>Likes: {item.likeCount}</LikeCount>

                    {/* 이미지가 있을 때 렌더링 */}
                    {item.imageUrls && item.imageUrls.length > 0 && (
                        <ImageContainer>
                            {item.imageUrls.map((url, index) => (
                                <Image key={index} src={url} alt={`Image ${index + 1}`} />
                            ))}
                        </ImageContainer>
                    )}
                </>
            ) : (
                <ErrorMessage>Item not found</ErrorMessage>
            )}
        </DetailContainer>
    );
}

const DetailContainer = styled.div`
    max-width: 800px;
    margin: 100px auto;
    padding: 20px;
    background-color: #f9f9f9;
    border-radius: 10px;
    box-shadow: 0px 4px 15px rgba(0, 0, 0, 0.1);
`;

const Title = styled.h1`
    font-size: 36px;
    font-weight: bold;
    color: #333;
    margin-bottom: 20px;
`;

const Content = styled.p`
    font-size: 20px;
    line-height: 1.6;
    color: #555;
    margin-bottom: 30px;
`;

const LikeCount = styled.p`
    font-size: 24px;
    font-weight: bold;
    color: #007bff;
`;

const LoadingMessage = styled.p`
    font-size: 24px;
    color: #666;
    text-align: center;
`;

const ErrorMessage = styled.p`
    font-size: 24px;
    color: red;
    text-align: center;
`;

/* 이미지 관련 스타일 추가 */
const ImageContainer = styled.div`
    display: flex;
    flex-wrap: wrap;
    gap: 10px;
    margin-top: 20px;
`;

const Image = styled.img`
    max-width: 100%;
    height: auto;
    border-radius: 10px;
    box-shadow: 0px 4px 8px rgba(0, 0, 0, 0.1);
`;
