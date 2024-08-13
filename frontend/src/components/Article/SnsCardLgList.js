import React, { useState, useEffect } from "react";
import API from '../../apis/api';
import useAuthStore from '../../stores/member';
import SnsCardLg from './SnsCardLg';
import qs from 'qs';

const SnsCardLgList = ({ searchId, tagTypeList, selectedVisibility }) => {
  const authSearchId = useAuthStore((state) => state.getSearchId());

  const [snslist, setSnsList] = useState([]);
  const [page, setPage] = useState(0);
  const [hasMore, setHasMore] = useState(true);
  const [loading, setLoading] = useState(false);

  const fetchSnsList = async (searchId, page) => {
    setLoading(true);
    try {
      const params = {
        searchId,
        page,
        tagType: tagTypeList,
        neighborType: selectedVisibility
      };
      const queryString = qs.stringify(params, { arrayFormat: 'comma', skipNulls: true });
      const url = `/user/sns?${queryString}`;
      console.log('%^%^%^%^Request URL:', url);

      const response = await API.get(url);
      console.log('API response:', response.data);
      if (response.data.length === 0) {
        setHasMore(false);
        if (page === 0) {
          setSnsList([]);
        }
      } else {
        if (page === 0) {
          setSnsList(response.data);
        } else {
          setSnsList((prevSnsList) => [...prevSnsList, ...response.data]);
        }
        setPage(page + 1);
      }
    } catch (error) {
      console.error('Fetch SnsList Error:', error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchSnsList(searchId, 0);
  }, [searchId, tagTypeList, selectedVisibility]);

  const handleScroll = () => {
    if (window.innerHeight + document.documentElement.scrollTop >= document.documentElement.offsetHeight - 10 && hasMore && !loading) {
       fetchSnsList(searchId, page);
    }
  };
 

  useEffect(() => {
    window.addEventListener('scroll', handleScroll);
    return () => window.removeEventListener('scroll', handleScroll);
  }, [hasMore, page, loading]);

  return (
    <div>
      {snslist.map((sns) => (
        <SnsCardLg 
          key={sns.articleId}
          articleId={sns.articleId}
          nickname={sns.nickname}
          image={sns.image}
          content={sns.content}
          likeCnt={sns.likeCnt}
          commentCnt={sns.commentCnt}
          isLiked={sns.isLiked}
          isBookmarked={sns.isBookmarked}
          createdAt={sns.createdAt}
          profile={sns.profile}
          searchId={sns.searchId}
        />
      ))}
      {loading && <div>Loading...</div>}
    </div>
  );
}

export default SnsCardLgList;
