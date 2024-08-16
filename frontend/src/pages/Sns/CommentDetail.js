import React, { useEffect, useState } from "react";
import { useLocation } from "react-router-dom";

import Comment from "../../components/Sns/Comment";


const CommentDetail = () => {
  const location = useLocation();
    const { articleId } = location.state;

  return (
    <div>
      <Comment articleId={articleId}/>
    </div>
  );
}

export default CommentDetail;