package com.team.pretLancer_7.controller;

public class test {

	public static int getLevenshteinDistance(String X, String Y)
    {

        int m = X.length();
        int n = Y.length();
 
        int[][] T = new int[m + 1][n + 1];
        for (int i = 1; i <= m; i++) {
            T[i][0] = i;
        }
        for (int j = 1; j <= n; j++) {
            T[0][j] = j;
        }
 
        int cost;
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                cost = X.charAt(i - 1) == Y.charAt(j - 1) ? 0: 1;
                T[i][j] = Integer.min(Integer.min(T[i - 1][j] + 1, T[i][j - 1] + 1),
                        T[i - 1][j - 1] + cost);
            }
        }
 
        return T[m][n];
    }
 
    public static double findSimilarity(String x, String y) {
		
        if (x == null || y == null) {
            throw new IllegalArgumentException("Strings must not be null");
        }
        
        double maxLength = Double.max(x.length(), y.length());
        if (maxLength > 0) {
            // 필요한 경우 선택적으로 대소문자를 무시합니다.
            return (maxLength - getLevenshteinDistance(x, y)) / maxLength;
        }
        return 1.0;
    }
 
    public static void main(String[] args) {
        double similarity = findSimilarity("오늘은 비가 안 올 것 같아서 집에 먼저 갈게요.", "오늘은 비가 오지 않을 것 같으니 집에 먼저 갈래요.");
        System.out.println(similarity);
    }
	
}
