<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<h1>김우진 전화번호 수정페이지</h1>
	<p>수정 화면 입니다.
	   아래항목을 수정 하시고 "수정" 버튼을 눌러주세요	</p>
	   
	<Form action="/phonebook4/phone/modify" method="get">
	이름(name)<input type="text" name="name" value="${personVo.name }"><br>
	핸드폰(hp)<input type="text" name="hp" value="${personVo.hp }"><br>
	회사(company)<input type="text" name="company" value="${personVo.company }"><br>
	<input type="hidden" name="personid" value="${personVo.personid }"><br>
	<button type="submit">수정</button>
	</Form>
</body>
</html>