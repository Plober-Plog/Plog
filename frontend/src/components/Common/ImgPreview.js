import React from 'react';
import PropTypes from 'prop-types';
<<<<<<< HEAD
import Btn from './Btn';

const ImgPreview = ({ src, onDelete, isDisabled }) => {
  return (
    <div style={{ display: 'inline-block', margin: '10px' }}>
      <img src={src} alt="preview" style={{ width: '100px', height: '100px', objectFit: 'cover' }} />
      <div>
        <Btn 
          content="X" 
          type="button" 
          onClick={onDelete} 
          style={{ 
            background: isDisabled ? 'gray' : null,
            cursor: isDisabled ? 'not-allowed' : 'pointer'
          }}
          disabled={isDisabled}
        />
      </div>
=======

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
>>>>>>> master
    </div>
  );
};

ImgPreview.propTypes = {
  src: PropTypes.string.isRequired,
  onDelete: PropTypes.func.isRequired,
<<<<<<< HEAD
  isDisabled: PropTypes.bool,
};

ImgPreview.defaultProps = {
  isDisabled: false,
=======
>>>>>>> master
};

export default ImgPreview;
