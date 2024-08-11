import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import API from '../../apis/api';
import useAuthStore from '../../stores/member';
import PropTypes from 'prop-types';

const CommentItem = ({ comment, handleReply }) => {
  const navigate = useNavigate();
  const { userData } = useAuthStore();

  // 댓글 삭제
  const handleCmtDelete = async (commentId) => {
    try {
      const response = await API.delete(`/user/sns/comment`, {
        params: { commentId }
      });
      console.log('댓글 삭제 성공:', response.data);
    } catch (error) {
      console.error('댓글 삭제 실패:', error.response);
    }
  };

  // 자식 댓글이면 들여쓰기
  const divStyle = comment.parentId !== comment.articleCommentId
    ? { marginLeft: '20px' }
    : {};

  // 날짜 시간 수정
  const formattedDate = comment.createDate.slice(0, 16);

  return (
    <div style={divStyle}>
      {comment.state === 1 ? (
        <>
          <img src={comment.profile} alt="profile" onClick={() => navigate(`/profile/${comment.searchId}`)} />
          <h4>{comment.nickname}</h4>
          <p>{comment.content}</p>
          <p>{formattedDate}</p>
          {comment.parentId === comment.articleCommentId && (
            <button onClick={() => handleReply(comment.articleCommentId)}>답글작성</button>
          )}
          {comment.searchId === userData.searchId && (
            <button onClick={() => handleCmtDelete(comment.articleCommentId)}>댓글 삭제</button>
          )}
        </>
      ) : (
        <p>삭제된 댓글입니다</p>
      )}
    </div>
  );
};

CommentItem.propTypes = {
  comment: PropTypes.shape({
    commentId: PropTypes.number.isRequired,
    articleCommentId: PropTypes.number.isRequired,
    profile: PropTypes.string,
    nickname: PropTypes.string,
    createDate: PropTypes.string,
    content: PropTypes.string,
    state: PropTypes.number.isRequired,
  }).isRequired,
  handleReply: PropTypes.func.isRequired,
};

export default CommentItem;
