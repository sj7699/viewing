import React from "react";
import styled from "styled-components";
import { Link } from "react-router-dom";

const Container = styled.div`
  width: 900px;
  height: 50px;
  /* background-color: var(--gray-100); */
  border: 0.5px var(--gray-100) solid;

  display: flex;
  font-size: 14px;
  align-items: center; /* Center items vertically */
`;

const Checkbox = styled.input.attrs({ type: "checkbox" })`
  margin-left: 10px;
`;

const NameContainer = styled.div`
  flex: 1;
  display: flex;
  align-items: center;
  padding-left: 10px;
  color: #000000;
  font-weight: 300;
  
`;

const Name = styled.div`
  width: 150px;
  text-overflow: ellipsis;
  white-space: nowrap;
  overflow: hidden;
`

const TitleContainer = styled(Link)`
  flex: 3;
  display: flex;
  align-items: center;
  justify-content: flex-start;
  padding-left: 10px;
  color: #000000;
  font-weight: 300;
  
`;

const DateContainer = styled.div`
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: flex-end;
  padding-right: 10px;
  color: var(--gray-300);
  font-weight: 100;
`;

export default function MessageBox({ message_id, name, title, date, deleted, setDeleted }) {

  const handleCheck = () => {
    if (deleted.includes(message_id)) {
      const temp = deleted.filter(d => d !== message_id)
      setDeleted(temp)
    } else {
      deleted.push(message_id)
      setDeleted([...deleted])
    }
  }

  return (
    <Container>
      <Checkbox onChange={handleCheck} checked={deleted.includes(message_id)}/>
      <NameContainer><Name>{name}</Name> </NameContainer>
      <TitleContainer to={`${message_id}`}>{title}</TitleContainer>
      <DateContainer>{date}</DateContainer>
    </Container>
  );
}
