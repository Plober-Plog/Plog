import React from 'react';
import { useHistory } from 'react-router-dom';

const HeaderBackBtn = () => {
  let history = useHistory();

  return (
    <button onClick={() => history.goBack()}>
      뒤로가기
    </button>
  )
}

export default HeaderBackBtn;