import React, { useState } from 'react';
import SnsCardLgList from '../../components/Article/SnsCardLgList';
import Tags from '../../components/Sns/Tags';
import Tab from '../../components/Sns/Tab';
import AddBtn from '../../components/Common/AddBtn';
import useAuthStore from '../../stores/member';

const SnsList = () => {
  const isLogin = useAuthStore(state => state.isLogin);
  const [tagTypeList, setTagTypeList] = useState([]);
  const [selectedVisibility, setSelectedVisibility] = useState(1);
  const tags = [
    { tagTypeId: 1, tagName: '일지' },
    { tagTypeId: 2, tagName: '분석 레포트' },
    { tagTypeId: 3, tagName: '질문' },
    { tagTypeId: 4, tagName: '일기' },
    { tagTypeId: 5, tagName: '식물' },
    { tagTypeId: 6, tagName: '정보' },
  ];

  const handleTagSelect = (id) => {
    setTagTypeList(prevTags =>
      prevTags.includes(id) ? prevTags.filter(tag => tag !== id) : [...prevTags, id]
    );
  };

  return (
    <div>
      <div className='pb-5'>
        {isLogin && (
          <div className='pb-12'>
            <Tab
              selectedVisibility={selectedVisibility}
              setSelectedVisibility={setSelectedVisibility}
            />
          </div>
        )}
      </div>
      <Tags 
        selectedTags={tagTypeList}
        onTagSelect={handleTagSelect}
        tags={tags}
      />
      <AddBtn 
        type='sns'
      />
      <SnsCardLgList 
        tagTypeList={tagTypeList}
        selectedVisibility={selectedVisibility}
      />
    </div>
  );
}

export default SnsList;
