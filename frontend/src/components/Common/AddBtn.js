import React from 'react';
import { useNavigate } from 'react-router-dom';

const AddBtn = ({ type }) => {
  const navigate = useNavigate();

  const handleClick = () => {
    if (type === 'plant') {
      navigate('/plant/register', {
        state: {"plantId": 0}
      });
    } else if (type === 'sns') {
      navigate('/sns/write', {
        state: {"articleId": 0}
      });
    }
  }

  return (
    <button onClick={handleClick}>
      {type === 'plant' ? '식물 등록' : '게시글 등록'}
    </button>
  )
}

export default AddBtn;