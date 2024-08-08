import React, { useState } from 'react';
import SnsCardLgList from '../../components/Article/SnsCardLgList';
import Tags from '../../components/Sns/Tags';

const SnsList = () => {
  const [tagTypeList, setTagTypeList] = useState([]);
  const tags = [
    { tagTypeId: 1, tagName: '일지' },
    { tagTypeId: 2, tagName: '분석 레포트' },
    { tagTypeId: 3, tagName: '질문' },
    { tagTypeId: 4, tagName: '일기' },
    { tagTypeId: 5, tagName: '식물' },
    { tagTypeId: 6, tagName: '정보' },
    { tagTypeId: 7, tagName: '룸꾸미기' }
  ];

  const handleTagSelect = (id) => {
    setTagTypeList(prevTags =>
      prevTags.includes(id) ? prevTags.filter(tag => tag !== id) : [...prevTags, id]
    );
  };

  console.log('Selected tagTypeList:', tagTypeList);  // tagTypeList가 제대로 업데이트되고 있는지 확인

  return (
    <div>
      Sns List Page 입니당
      <Tags 
        selectedTags={tagTypeList}
        onTagSelect={handleTagSelect}
        tags={tags}
      />
      <SnsCardLgList 
        tagTypeList={tagTypeList}
      />
    </div>
  );
}

export default SnsList;
