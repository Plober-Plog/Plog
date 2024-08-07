import React from 'react';

const Tags = ({ selectedTags, onTagSelect, tags }) => {
  return (
    <div style={{ display: 'flex', gap: '10px' }}>
      {tags.map(tag => (
        <button
          key={tag.id}
          onClick={() => onTagSelect(tag.id)}
          style={{
            padding: '10px 20px',
            borderRadius: '20px',
            border: 'none',
            backgroundColor: selectedTags.includes(tag.id) ? '#a5d6a7' : '#e0e0e0',
            color: selectedTags.includes(tag.id) ? 'white' : 'black',
            cursor: 'pointer'
          }}
        >
          {tag.label}
        </button>
      ))}
    </div>
  );
};

export default Tags;
