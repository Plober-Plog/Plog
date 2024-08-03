import React from 'react';
import PropTypes from 'prop-types';
import Btn from './Btn';
import Img from './Img';

const ImgPreview = ({ src, onDelete }) => {
  return (
    <div style={{ position: 'relative', display: 'inline-block', margin: '10px' }}>
      <Img src={src} alt="preview"
       style={{ 
        width: '80px', 
        height: '80px', 
        objectFit: 'cover', 
        borderRadius: '8px' }} 
        />
      {/* 이미지가 너무 크게 보이지 않게 스타일 잠깐 적용함 */}
      <Btn content="X" type="button" onClick={onDelete} 
      style={{
        position: 'absolute',
        top: '0px',
        right: '0px',
        backgroundColor: 'transparent',
        color: 'black',
        border: 'none',
        fontWeight: 'semibold',
        fontSize: '16px',
        cursor: 'pointer'
      }}
      />
    </div>
  );
};

ImgPreview.propTypes = {
  src: PropTypes.string.isRequired,
  onDelete: PropTypes.func.isRequired,
};

export default ImgPreview;