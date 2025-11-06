import { useState, useEffect } from 'react';

export default function useFetch(fn, deps = []) {
  const [data, setData] = useState(null);
  const [error, setError] = useState();
  useEffect(() => {
    fn().then(setData).catch(setError);
  }, deps);
  return { data, error };
}
