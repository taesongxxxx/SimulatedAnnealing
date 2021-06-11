# 모의 담금질 기법


![Hill_Climbing_with_Simulated_Annealing](https://user-images.githubusercontent.com/82091824/120922713-aad85400-c705-11eb-9a9a-74e07b66f818.gif)

**모의 담금질 기법**(Simulated Annealing)은 해 탐색 알고리즘으로 어원은 담금질(quenching)에서 왔는데 이는 풀림(annealing)의 오역이다.
풀림은 금속재료를 가열한 다음 조금씩 냉각해 결정을 성장시켜 그 결함을 줄이는 작업으로 모의 담금질 기법은 이 과정을 모방한다.
높은 온도에서 해를 탐색할때는 특정한 패턴없이 자유분방하게 이루어지지만, 온도가 점차 낮아짐에따라 해 탐색이 점점 규칙적인 방식으로 이루어진다.

▼ 담금질 기법을 이용한 전역 최적해를 구하는 과정

![image](https://user-images.githubusercontent.com/82091824/121193672-00009b00-c8a9-11eb-91a2-2dc8c25929cb.png)


(1) 후보해를 설정한 뒤 이웃해와 비교하여 더 우수한 해를 후보해로 둔다.

(2) 이 과정을 반복하여 계속 우수한 해를 찾아간다.

하지만 이렇게 이웃해만 비교하여 최적점을 찾다보면 지역최적점은 쉽게 찾을수 있겠지만, 그 점이 전역 최적점일 확률은 극히 낮을 것이다.

모의 담금질 기법은 여기에서 확률 개념을 도입하여 전역 최적점을 찾을 확률을 높인다.

적당한 확률 p를 설정한 뒤 0~1까지 주사위를 굴려 나온 값이 p보다 작으면 임의의 점을 구해 그 값이 후보해보다 더 우수하지 않더라도 그 점으로
이동하는 자유분방함을 보여준다. 이렇게하면 전역 최적점을 구하는데 걸리는 시간은 훨씬 많이 걸리지만 전역 최적해를 찾을 확률은
높아진다.


다음은 java에서 구현한 모의 담금질 기법의 기본적인 알고리즘이다.
```java 
      for (double t = 100; t > 10; t = t * a){
            int kt = (int) t;
            for(int j=0; j<kt; j++){
                double upper = x0 + 0.1;
                double lower = x0 - 0.1;
                double x1 = r.nextDouble()  * (upper - lower) + lower;
                double f1 = p.fit(x1);

                if(p.isNeighborBetter(f0,f1)){
                    x0 = x1;
                    f0 = f1;
                    hist.add(f0);
                }else{
                    double d = Math.abs(f1 - f0);
                    double p0 = Math.exp(-d/t);
                    if(r.nextDouble() < p0) {
                        x0 = x1;
                        f0 = f1;
                        hist.add(f0);
                    }
                }
            }
        }
        return x0;
```

t(온도)를 너무 급속히 낮추면 전역최적점에 도달할 확률이 낮아지고, 너무 천천히 낮추면 적역최적점에 도달할 확률은 높아지지만
반복횟수가 많아져 그만큼 시간이 오래 걸린다. 그래서 적절한 a(냉각율)을 설정하는게 중요하다.

kt는 t에서의 반복횟수로 t가 작아질수록, kt 또한 작아지게 조절한다. 만약 kt의 값이 크면 그만큼 시간이 오래걸리므로 적절한 값을
실험을 통해 찾아준다.

p0는 자유롭게 탐색할 확률로 초기에는 탐색이 자유롭다가 점차 규칙적으로 변하므로, t가 작아짐에 따라 p0도 작아지게 조절한다.
p0에 반영시켜야 할 또 하나의 요소는 f1과 f0의 차이인 d값인데 d값이 크면 p0를 작게하고, d값이 작으면 p0를 크게한다. 그 이유는
값의 차이가 큼에도 불구하고 p0를 크게하면 그 동한 탐색한 결과가 무시되어 랜덤하게 탐색하는 결과를 낳기 떄문이다. 이 2가지 요소를 종합하여
확률 p0를 다음과 같이 정의 할 수 있다. p0 = e^-(d/t)

d 값이 t에 비해 너무 크면 p0가 d에 훨씬 민감하게 반응할 수 있으므로, 이웃해를 충분히 가깝게 설정하여 d 값을 조절해준다.

## 실험

주어진 함수:![CodeCogsEqn (42)](https://user-images.githubusercontent.com/82091824/121331635-07c84a00-c952-11eb-89ed-42423266a8ca.gif)
, x = 6에서 전역최적해를 가짐.

![asdr](https://user-images.githubusercontent.com/82091824/121332846-34c92c80-c953-11eb-8467-dd483d0a69d6.png)

초기온도를 100으로 설정한 뒤 a, kt를 조절하면서 성능을 비교해보자.

각각 100번 실행한 뒤 오차율과 걸린시간의 평균을 내봤다.

오차율 = (이론값 - 실험값) / 이론값 x 100

### t > 10 까지 반복

1. a=0.99, kt=t*20

```
오차율 : 0.3895%

걸린시간 : 36.88ms
```

2. a=0.99, kt=t*10

```
오차율 : 0.437%

걸린시간 : 25ms
```

3. a=0.99, kt=t

```
오차율 : 0.6545%

걸린시간 : 6.3ms
```

4. a=0.9, kt=t*20

```
오차율 : 0.66975%
 
걸린시간 : 8.38ms
```

5. a=0.9, kt=t*10

```
오차율 : 0.6848%

걸린시간 : 6ms
```

6. a=0.9, kt=t

```
오차율 : 0.7412%

걸린시간 : 1.76ms
```
7. a=0.8, kt=t*20

```
오차율 : 0.5135%

걸린시간 : 5.34ms
```


8. a=0.8, kt=t*10

```
오차율 : 0.6933% 

걸린시간 : 3.6ms
```

9. a=0.8, kt=t

```
오차율 : 0.8592%

걸린시간 : 1.2ms
```

## 결론

a 와 kt 가 낮아질때 걸리는 시간의 차이에 비해 오차율의 차이는 크지않아 a = 0.8, kt = t 를 선택함.

![image](https://user-images.githubusercontent.com/82091824/121675163-03d12f00-caee-11eb-9375-71c0eb51dff6.png)



### References
■ https://ko.wikipedia.org/wiki/%EB%8B%B4%EA%B8%88%EC%A7%88_%EA%B8%B0%EB%B2%95   
■ https://en.wikipedia.org/wiki/Simulated_annealing
