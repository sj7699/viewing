import { styled } from "styled-components";
import { Link, Outlet, useLocation, useNavigate } from "react-router-dom";
import StudySideBar from "../Study/StudySideBar";
import { useEffect, useState } from "react";

const Container = styled.div`
  display: flex;
  padding: 20px;
`;

const SidebarContainer = styled.div`
  width: 222px;
  height: 600px;

  margin-right: 50px;
  border-top: 1px solid #ebebebde;
`;

const Menu = styled.div`
  width: 150px;
  height: 30px;
  font-weight: 300;
  padding-top: 13px;
  padding-left: 15px;
  color: var(--gray-800);
  border-bottom: 1px solid #ebebebde;
  text-decoration: none;
  cursor: pointer;

  &:hover {
    background-color: var(--secondary);
  }
  ${({ $active }) =>
    $active &&
    `
    background-color: var(--secondary);
  `}
`;

const SubMenu = styled.div`
  display: ${(props) => {
    if (props.$dropdown) {
      return "block";
    } else {
      return "none";
    }
  }};
  width: 125px;
  height: 25px;
  font-weight: 300;
  padding-top: 13px;
  padding-left: 15px;
  color: var(--gray-800);
  border-bottom: 1px solid #ebebebde #d1cef2;
  text-decoration: none;
  padding-left: 40px;
  font-size: 14px;
  cursor: pointer;

  ${({ $active }) =>
    $active &&
    `
    background-color: #d1cef2;
  `}
`;

const MyPage = styled.div`
  width: 150px;
  height: 30px;
  font-weight: 300;
  padding-top: 13px;
  padding-left: 15px;
  color: var(--gray-800);
  border-bottom: 1px solid #ebebebde;
  text-decoration: none;
  background-color: var(--secondary);
`;

const StyledLink = styled(Link)`
  text-decoration: none;
  cursor: pointer;
`;

export default function SideBar() {
  const navigate = useNavigate();
  const location = useLocation();
  const [clicked, setClicked] = useState(location.pathname);
  const [subClicked, setSubClicked] = useState(false);

  useEffect(() => {
    if (location.pathname === "/mypage") {
      navigate("/mypage/edit");
    }

    if (location.pathname === "/mypage/get") {
      setClicked("내 쪽지함");
      setSubClicked("get");
    } else if (location.pathname === "/mypage/send") {
      setSubClicked("send");
    } else {
      setClicked(location.pathname);
    }
  }, [location, navigate]);

  function handleClick(menu) {
    setSubClicked(false);
    setClicked(menu);
  }

  const menuList = [
    "내 정보 수정",
    "내 스터디",
    "찜한 스터디",
    "내 쪽지함",
    "내 캘린더",
    "내가 쓴 글",
    "내가 좋아요 한 글",
    "내가 댓글 단 게시글",
    "탈퇴하기",
  ];
  const linkList = [
    "/mypage/edit",
    "/mypage/study",
    "/mypage/bookmark",
    "/mypage/get",
    "/mypage/calendar",
    "/mypage/myarticle",
    "/mypage/like",
    "/mypage/comment",
    "/mypage/withdraw",
  ];
  const menuListDoms = menuList.map((menu, idx) => {
    if (menu === "내 쪽지함" && clicked) {
      return (
        <div key={idx}>
          <Menu
            $active={clicked === (menu || "/mypage/get" || "/mypage/send")}
            onClick={() => setClicked(menu)}
          >
            {menu}
          </Menu>
          <StyledLink to={"/mypage/get"}>
            <SubMenu
              $dropdown={clicked === menu}
              onClick={() => setSubClicked("get")}
              $active={subClicked === "get"}
            >
              받은 쪽지함
            </SubMenu>
          </StyledLink>
          <StyledLink to={"/mypage/send"}>
            <SubMenu
              $dropdown={clicked === menu}
              onClick={() => setSubClicked("send")}
              $active={subClicked === "send"}
            >
              보낸 쪽지함
            </SubMenu>
          </StyledLink>
        </div>
      );
    } else {
      return (
        <StyledLink
          to={linkList[idx]}
          onClick={() => handleClick(linkList[idx])}
          key={idx}
        >
          <Menu $active={clicked === linkList[idx]}>{menu}</Menu>
        </StyledLink>
      );
    }
  });

  return (
    <Container>
      <SidebarContainer>
        <MyPage>마이 페이지</MyPage>
        {menuListDoms}
      </SidebarContainer>
      <div>
        <Outlet />
      </div>
    </Container>
  );
}
