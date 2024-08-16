import React from 'react';
import { useNavigate } from 'react-router-dom';
import './AddBtn.css'
import leafIcon from '../../assets/icon/leaf-white.png';
import docsIcon from '../../assets/icon/docs-white.png';

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
      {type === 'plant' ? <img src={leafIcon}/> : <img src={docsIcon}/>}
    </button>
  )
}

export default AddBtn;