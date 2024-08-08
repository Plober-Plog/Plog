import React, { useRef } from 'react';
import PropTypes from 'prop-types';
import DiaryTodoIcon from '../../components/Diary/DiaryTodoIcon'; 
import ImgPreview from '../../components/Common/ImgPreview'; 
import Btn from '../../components/Common/Btn'; 

const ImgUpload = ({ cameraIcon, imgs, handleImageUpload, handleDeleteImage, isDisabled }) => {
  const fileInputRef = useRef(null);

  const handleClick = () => {
    if (!isDisabled) {
      fileInputRef.current.click();
    }
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
          filter: isDisabled ? 'grayscale(100%)' : 'none' // 비활성화 시 회색으로 변경
        }}
        disabled={isDisabled}
      />
      <input
        type="file"
        ref={fileInputRef}
        style={{ display: 'none' }}
        multiple
        accept="image/*"
        capture="environment"
        onChange={(e) => {
          if (!isDisabled) {
            handleImageUpload(e);
            e.target.value = '';
          }
        }}
        disabled={isDisabled}
      />
      <div>
        {imgs.map((img, index) => (
          <ImgPreview 
            key={index} 
            src={img.url} 
            onDelete={() => handleDeleteImage(index)} 
            isDisabled={isDisabled} // 비활성화 상태 전달
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
  isDisabled: PropTypes.bool,
};

ImgUpload.defaultProps = {
  isDisabled: false,
};

export default ImgUpload;
