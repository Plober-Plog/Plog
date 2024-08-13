import React from 'react';
import PropTypes from 'prop-types';

const ImgPreview = ({ src, onDelete }) => {
  return (
    <div style={{ position: 'relative', display: 'inline-block', margin: '5px' }}>
      <img 
        src={src} 
        alt="preview" 
        style={{ 
          width: '70px', 
          height: '70px', 
          objectFit: 'cover', 
          borderRadius: '8px' 
        }} 
      />
      <button 
        onClick={onDelete} 
        style={{
          position: 'absolute',
          top: '0px',
          right: '0px',
          border: 'none',
          borderRadius: '50%',
          width: '20px',
          height: '20px',
          display: 'flex',
          alignItems: 'center',
          justifyContent: 'center',
          cursor: 'pointer',
          padding: 0,
          fontSize: '14px', 
          color: '#333', 
        }}
      >
        X
      </button>
    </div>
  );
};

ImgPreview.propTypes = {
  src: PropTypes.string.isRequired,
  onDelete: PropTypes.func.isRequired,
};

export default ImgPreview;
