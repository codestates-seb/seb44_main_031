import { useEffect, useRef } from 'react';

const useDidMountEffect = (func: any, deps: any) => {
  const didMount = useRef(false);

  useEffect(() => {
    if (didMount.current) func();
    else didMount.current = true;
  }, [func, deps]);
};

export default useDidMountEffect;
