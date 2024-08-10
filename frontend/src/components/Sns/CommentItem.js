import React from 'react';
import { useNavigate } from'react-router-dom';
import API from '../../apis/api';
import useAuthStore from '../../stores/member';
import PropTypes from 'prop-types';

const CommentItem = ({ comments, handleReply }) => {
  const searchId = useAuthStore((state) => state.getSearchId());
  const navigate = useNavigate();

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

  return (
    <div>
      {comments.map((comment, index) => (
        <div 
          key={comment.commentId} 
          style={{ 
            opacity: comment.state === 1 ? 1 : 0.5, 
            marginLeft: index !== 0 ? '20px' : '0' 
          }}
        >
          {comment.state === 1 ? (
            <>
              <img src={comment.profile} alt="profile" onClick={() => navigate(`/profile/${comment.searchId}`)}/>
              <h4>{comment.nickname}</h4>
              <p>{comment.content}</p>
              <p>{comment.createDate}</p>
              {index === 0 && (
                <button onClick={() => handleReply(comment.articleCommentId)}>답글작성</button>
              )}
              {/* TODO userId가 같으면 삭제 출력 */}
              {comment.searchId === searchId && (
                <button onClick={() => handleCmtDelete(comment.articleCommentId)}>댓글 삭제</button>
              )}
            </>
          ) : (
            <p>삭제된 댓글입니다</p>
          )}
          <hr />
        </div>
      ))}
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
      state: PropTypes.number.isRequired,
    })
  ).isRequired,
  handleReply: PropTypes.func.isRequired,
};

export default CommentItem;
