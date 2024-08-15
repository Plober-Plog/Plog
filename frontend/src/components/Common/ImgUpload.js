import React, { useRef } from 'react';
import PropTypes from 'prop-types';
import ImgPreview from './ImgPreview'; 
import DiaryTodoIcon from '../Diary/DiaryTodoIcon';

const ImgUpload = ({ cameraIcon, imgs, handleImageUpload, handleDeleteImage }) => {
  const fileInputRef = useRef(null);

  const handleClick = () => {
    fileInputRef.current.click();
  };

  return (
    <div className="img-upload-container">
      <button 
        onClick={handleClick}
        className="img-upload-button"
      >
        <DiaryTodoIcon src={cameraIcon} alt="upload" />
      </button>
      <input
        type="file"
        ref={fileInputRef}
        style={{ display: 'none' }}
        multiple
        accept="image/*"
        onChange={(e) => {
          handleImageUpload(e);
          e.target.value = ''; // Reset the input so that the same file can be uploaded again if needed
        }}
      />
      <div className="img-previews">
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
