import { render, screen } from '@testing-library/react';
import App from './App';

test('renders Editor App title', () => {
  render(<App />);
  const linkElement = screen.getByText(/Editor App/i);
  expect(linkElement).toBeInTheDocument();
});
