import React from 'react';
<<<<<<< HEAD

// const tags = [
  //   { id: 1, label: '일지' },
  //   { id: 2, label: '분석 레포트' },
  //   { id: 3, label: '질문' },
  //   { id: 4, label: '일기' },
  //   { id: 5, label: '식물' },
  //   { id: 6, label: '정보' },
  //   { id: 7, label: '룸꾸미기' }
  // ];
  ////////////// 선택된 태그들, 태그선택, 태그 옵션
const Tags = ({ selectedTags, onTagSelect, tags }) => {
  return (
    <div>
      {tags.map(tag => (
        <button
          key={tag.id}
          onClick={() => onTagSelect(tag.id)}
          style={{color: selectedTags.includes(tag.id) ? 'gray' : 'black'}}
        >
          {tag.label}
=======
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
>>>>>>> master
        </button>
      ))}
    </div>
  );
};

export default Tags;
