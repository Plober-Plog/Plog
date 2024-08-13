import React from 'react';
import { useNavigate } from 'react-router-dom';
import API from '../../apis/api';
import useAuthStore from '../../stores/member';
import PropTypes from 'prop-types';
import '../../pages/Sns/CommentDetail.css';
import arrow from '../../assets/icon/arrow.png'; // 화살표 아이콘 추가

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

  // 자식 댓글이면 화살표 추가
  const divStyle = comment.parentId !== comment.articleCommentId
    ? 'comment-child'
    : '';

  // 날짜 시간 수정
  const formattedDate = comment.createDate.slice(0, 10);

  return (
    <div className={`comment-item ${divStyle}`}>
      {comment.state === 1 ? (
        <>
          {comment.parentId !== comment.articleCommentId && (
            <img src={arrow} alt="arrow icon" className="comment-arrow-icon" /> 
          )}
          <img 
            src={comment.profile} 
            className="comment-profile-img" 
            alt="profile" 
            onClick={() => navigate(`/profile/${comment.searchId}`)} 
          />
          <div className="comment-content">
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
              <h4>{comment.nickname}</h4>
              {comment.searchId === userData.searchId && (
                <button 
                  className="comment-delete-button"
                  onClick={() => handleCmtDelete(comment.articleCommentId)}
                >
                  댓글 삭제
                </button>
              )}
            </div>
            <p>{comment.content}</p>
            <p className="comment-date">
              {formattedDate}
              {comment.parentId === comment.articleCommentId && (
                <button 
                  className="comment-reply-button"
                  onClick={() => handleReply(comment.articleCommentId)}
                >
                  답글작성
                </button>
              )}
            </p>
          </div>
        </>
      ) : (
        <>
          {comment.parentId !== comment.articleCommentId && (
            <img src={arrow} alt="comment arrow icon" className="comment-arrow-icon" /> 
          )}
          <p>삭제된 댓글입니다</p>
        </>
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
