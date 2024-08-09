import React, { useRef } from 'react';
import PropTypes from 'prop-types';
import DiaryTodoIcon from '../../components/Diary/DiaryTodoIcon'; 
import ImgPreview from '../../components/Common/ImgPreview'; 
import Btn from '../../components/Common/Btn'; 

const ImgUpload = ({ cameraIcon, imgs, handleImageUpload, handleDeleteImage }) => {
  const fileInputRef = useRef(null);

  const handleClick = () => {
      fileInputRef.current.click();
  };

  return (
    <div>
      <Btn 
        content={<DiaryTodoIcon src={cameraIcon} alt="upload" />}
        onClick={handleClick}
        style={{ 
          background: 'none', 
          border: 'none', 
          cursor: 'pointer',
        }}
      />
      <input
        type="file"
        ref={fileInputRef}
        style={{ display: 'none' }}
        multiple
        accept="image/*"
        onChange={(e) => {
            handleImageUpload(e);
            e.target.value = '';
        }}
      />
      <div>
        {imgs.map((img, index) => (
          <ImgPreview 
            key={index} 
            src={img.url} 
            onDelete={() => handleDeleteImage(index)} 
          />
        ))}
      </div>
    </div>
  );
};

ImgUpload.propTypes = {
  cameraIcon: PropTypes.string.isRequired,
  imgs: PropTypes.arrayOf(PropTypes.shape({
    url: PropTypes.string.isRequired,
  })).isRequired,
  handleImageUpload: PropTypes.func.isRequired,
  handleDeleteImage: PropTypes.func.isRequired,
};

export default ImgUpload;
