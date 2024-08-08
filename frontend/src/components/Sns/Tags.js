import React from 'react';

const Tags = ({ selectedTags, onTagSelect, tags }) => {
  return (
    <div>
      {tags.map(tag => (
        <button
          key={tag.tagTypeId}
          onClick={() => onTagSelect(tag.tagTypeId)}
          style={{color: selectedTags.includes(tag.tagTypeId) ? 'gray' : 'black'}}
        >
          {tag.tagName}
        </button>
      ))}
    </div>
  );
};

export default Tags;
