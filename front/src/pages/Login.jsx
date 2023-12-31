import { styled } from "styled-components";
import GithubButton from "../components/Button/GithubButton";
import GoogleButton from "../components/Button/GoogleButton";
import KakaoButton from "../components/Button/KakaoButton";
import { useSelector } from "react-redux";
import { useNavigate } from "react-router-dom";
import { UserReducer } from "./../modules/UserReducer/UserReducer";
import { useEffect } from "react";

const Container = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  height: 550px;
`;

const InnerContainer = styled.div``;

const H1 = styled.h1`
  text-align: center;
  font-size: 20px;
`;
const Copywrite = styled.h1`
  text-align: center;
  font-size: 40px;
  margin-top: 15px;
  margin-bottom: 30px;
  font-weight: 700;
`;

const ButtonsContainer = styled.div`
  display: flex;
  justify-content: center;
  margin-top: 15px;
  &:hover{
    cursor : pointer;
  }
`;

export default function Login() {
  const isLogin = useSelector((state) => state.UserReducer.userId);
  const navigate = useNavigate();

  useEffect(() => {
    if (isLogin) {
      navigate("/");
    }
  }, []);

  return (
    <Container>
      <InnerContainer>
        {/* <H1>로그인 / 회원가입</H1> */}
        <Copywrite>뷰잉에 오신 것을 환영합니다.</Copywrite>
        <ButtonsContainer>
          <KakaoButton></KakaoButton>
        </ButtonsContainer>
        <ButtonsContainer>
          <GoogleButton></GoogleButton>
        </ButtonsContainer>
        <ButtonsContainer>
          <GithubButton></GithubButton>
        </ButtonsContainer>
      </InnerContainer>
    </Container>
  );
}
