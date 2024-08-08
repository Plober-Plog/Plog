import React from 'react';
import PropTypes from 'prop-types';
import './Content.css';

const Content = ({ content, setContent }) => {
  const handleChange = (event) => {
    setContent(event.target.value);
  };

  return (
    <div>
      <textarea 
        value={content} 
        onChange={handleChange} 
        placeholder="글을 입력하세요."
        className="textarea"
      />
    </div>
  );
};

Content.propTypes = {
  content: PropTypes.string.isRequired,
  setContent: PropTypes.func.isRequired,
};

export default Content;