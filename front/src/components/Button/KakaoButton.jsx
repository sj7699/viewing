import styled from "styled-components";

const Container = styled.div`
  width: 250px;
  height: 50px;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: #F7E600;
  color: var(--gray-900);
  font-weight: 500;
  border-radius: 15px;
`;

export function LoginKakao() {
  const KAKAO_URL = "https://kauth.kakao.com/oauth/authorize";
  const queries = {
    response_type : "code",
    client_id: "2869d34ce0755e095b7d39e5eb3aeafb",
    redirect_uri: "http://localhost:3000/login/kakao"
  };

  const params = new URLSearchParams(queries).toString();

  window.location.href = `${KAKAO_URL}?${params}`
}


export default function KakaoButton() {

  

  return <Container onClick={LoginKakao}>Kakao로 시작하기</Container>
}