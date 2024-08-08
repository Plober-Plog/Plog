import React, { useState, useEffect } from 'react';
import PropTypes from 'prop-types';
import API from "../../apis/api";
import FooterCmt from '../Common/FooterCmt';
import CommentItem from './CommentItem';

const Comment = ({ articleId, userInfo }) => {
  const [commentList, setCommentList] = useState([]);
  const [selectedParentId, setSelectedParentId] = useState(0);

  // 댓글 불러오기
  useEffect(() => {
    const fetchComments = async () => {
      try {
        const response = await API.get(`/user/sns/${articleId}/comment`);
        setCommentList(response.data);
        console.log(response.data.length, '댓글 불러오기 성공!:', response.data);
      } catch (err) {
        console.error('댓글 불러오기 실패 : ', err);
      }
    };

    fetchComments();
  }, [articleId]);

  const handleReply = (parentId) => {
    setSelectedParentId(parentId);
  };

  return (
    <div>
      <div>
        {commentList.map((comments) => (
          <CommentItem key={comments.articleCommentId} comments={comments} handleReply={handleReply} />
        ))}
      </div>
      <FooterCmt
        articleId={articleId}
        selectedParentId={selectedParentId}
        profile={userInfo.profile}
        setCommentList={setCommentList}
      />
    </div>
  );
};

Comment.propTypes = {
  articleId: PropTypes.number.isRequired,
  userInfo: PropTypes.shape({
    profile: PropTypes.string,
  }).isRequired,
};

export default Comment;
