import React from 'react';
import API from '../../apis/api';
import PropTypes from 'prop-types';

const CommentItem = ({ comments, handleReply }) => {

  const handleCmtDelete = async (commentId) => {
    try {
      console.log('commentId:', commentId);
      const response = await API.delete(`/user/sns/comment`, { commentId});
      console.log('댓글 삭제 성공:', response.data);
    } catch (error) {
      console.error('댓글 삭제 실패:', error.response);
    }
  };

  return (
    <div>
      {comments.map((comment, index) => {
        if (index === 0) {
          return (
            <div key={comment.commentId}>
              <img src={comment.profile} alt="profile" />
              <h4>{comment.nickname}</h4>
              <p>{comment.content}</p>
              <p>{comment.createDate}</p>
              <button onClick={() => handleReply(comment.articleCommentId)}>답글작성</button>
              <button onClick={() => handleCmtDelete(comment.commentId)}>댓글 삭제</button>
              <hr />
            </div>
          );
        } else {
          return (
            <div key={comment.commentId} style={{ marginLeft: '20px' }}>
              <img src={comment.profile} alt="profile" />
              <h4>{comment.nickname}</h4>
              <p>{comment.content}</p>
              <p>{comment.createDate}</p>
              <button onClick={() => handleCmtDelete(comment.commentId)}>댓글 삭제</button>
              <hr />
            </div>
          );
        }
      })}
    </div>
  );
};

CommentItem.propTypes = {
  comments: PropTypes.arrayOf(
    PropTypes.shape({
      commentId: PropTypes.number.isRequired,
      articleCommentId: PropTypes.number.isRequired,
      profile: PropTypes.string,
      nickname: PropTypes.string,
      createDate: PropTypes.string,
      content: PropTypes.string,
    })
  ).isRequired,
  handleReply: PropTypes.func.isRequired,
};

export default CommentItem;
