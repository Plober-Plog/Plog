import React, { useState, useEffect } from "react";
import API from '../../apis/api';
import useAuthStore from '../../stores/member';
import SnsCardMd from './SnsCardMd';

const SnsCardMdList = ({ searchId }) => {
  const authSearchId = useAuthStore((state) => state.getSearchId());

  const [snslist, setSnsList] = useState([]);
  const [page, setPage] = useState(0);
  const [hasMore, setHasMore] = useState(true);
  const [loading, setLoading] = useState(false);

  const fetchSnsList = async (searchId, page) => {
    setLoading(true);
    try {
      const response = await API.get(`/user/sns`, {
        params: {searchId, page}
      });
      if (response.data.length === 0) {
        setHasMore(false);
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
  }, [searchId]);

  const handleScroll = () => {
    if (window.innerHeight + document.documentElement.scrollTop === document.documentElement.offsetHeight && hasMore && !loading) {
      fetchSnsList(searchId, page);
    }
  };

  useEffect(() => {
    window.addEventListener('scroll', handleScroll);
    return () => window.removeEventListener('scroll', handleScroll);
  }, [hasMore, page, loading]);

  return (
    <div>
      <h3>유저의 SNS 목록</h3>
      {snslist.map((sns) => (
        <SnsCardMd 
          key={sns.articleId}
          articleId={sns.articleId}
          nickname={sns.nickname}
          image={sns.image}
          content={sns.content}
          likeCnt={sns.likeCnt}
          commentCnt={sns.commentCnt}
          isLiked={sns.isLiked}
          isBookmarked={sns.isBookmarked}
        />
      ))}
      {loading && <div>Loading...</div>}
    </div>
  )
}

export default SnsCardMdList;