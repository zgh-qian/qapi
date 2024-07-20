/**
 * @see https://umijs.org/docs/max/access#access
 * */
export default function access(initialState: InitialState | undefined) {
  const {loginUser} = initialState ?? {};
  return {
    canUser: loginUser && loginUser.role === 'user',
    canAdmin: loginUser && loginUser.role === 'admin',
    // canAdmin: currentUser && currentUser.access === 'admin',
  };
}
