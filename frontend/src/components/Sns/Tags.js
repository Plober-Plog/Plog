import React from 'react';
import './Tags.css';

const Tags = ({ selectedTags, onTagSelect, tags }) => {
  return (
    <div className='tags-container'>
      {tags.map(tag => (
        <button
          key={tag.tagTypeId}
          className={`tag ${selectedTags.includes(tag.tagTypeId) ? 'active' : ''}`}
          onClick={() => onTagSelect(tag.tagTypeId)}
        >
          {tag.tagName}
        </button>
      ))}
    </div>
  );
};

export default Tags;
