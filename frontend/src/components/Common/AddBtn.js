import React from 'react';
import { useNavigate } from 'react-router-dom';
import './AddBtn.css'

const AddBtn = ({ type }) => {
  const navigate = useNavigate();

  const handleClick = () => {
    if (type === 'plant') {
      navigate('/plant/register', {
        state: {plantId: 0}
      });
    } else if (type === 'sns') {
      navigate('/sns/write', {
        state: {articleId: 0}
      });
    }
  }

  return (
    <button
      className='add-btn' 
      onClick={handleClick}>
      {type === 'plant' ? '🌱' : '💬'}
    </button>
  )
}

export default AddBtn;