import react, { useState } from 'react';
import SnsCardLgList from '../../components/Article/SnsCardLgList';
import Tags from '../../components/Sns/Tags';

const SnsList = () => {

  const [tagTypeList, setTagTypeList] = useState([]);
  const tags = [
    { id: 1, label: '일지' },
    { id: 2, label: '분석 레포트' },
    { id: 3, label: '질문' },
    { id: 4, label: '일기' },
    { id: 5, label: '식물' },
    { id: 6, label: '정보' },
    { id: 7, label: '룸꾸미기' }
  ];

  const handleTagSelect = (id) => {
    setTagTypeList(prevTags =>
      prevTags.includes(id) ? prevTags.filter(tag => tag !== id) : [...prevTags, id]
    );
  };

  return (
    <div>
      Sns List Page 입니당
      <Tags 
        selectedTags={tagTypeList}
        onTagSelect={handleTagSelect}
        tags={tags}
      />
      <SnsCardLgList 
        tagType={tagTypeList}
      />
    </div>
  )
}

export default SnsList;