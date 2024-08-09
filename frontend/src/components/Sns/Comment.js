import React, { useState, useEffect } from 'react';
import PropTypes from 'prop-types';
import API from "../../apis/api";
import FooterCmt from '../Common/FooterCmt';
import CommentItem from './CommentItem';
import useAuthStore from '../../stores/member';

const Comment = ({ articleId }) => {
  const [commentList, setCommentList] = useState([]);
  const [selectedParentId, setSelectedParentId] = useState(0);
  const [isFooterCmtActive, setIsFooterCmtActive] = useState(false);
  const { userData } = useAuthStore();

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
    setIsFooterCmtActive(true);
  };

  return (
    <div>
      <hr />
      <h3>댓글</h3>
      <div>
        {commentList.map((comment) => (
          <CommentItem key={comment.articleCommentId} comments={comment} handleReply={handleReply} />
        ))}
      </div>
      <FooterCmt
        articleId={articleId}
        selectedParentId={selectedParentId}
        setSelectedParentId={setSelectedParentId}
        profile={userData.profile}
        setCommentList={setCommentList}
        isActive={isFooterCmtActive}
        setIsActive={setIsFooterCmtActive}
      />
    </div>
  );
};

Comment.propTypes = {
  articleId: PropTypes.number.isRequired,
};

export default Comment;
